package com.runningtechie.transparentrunning.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.runningtechie.transparentrunning.database.dao.LocationPointDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionDao
import com.runningtechie.transparentrunning.model.LocationPoint
import com.runningtechie.transparentrunning.model.WorkoutSession

private const val DATABASE_NAME = "transparent-running-database"

class TransparentRunningRepository private constructor() {
    companion object {
        private lateinit var workoutSessionDao: WorkoutSessionDao
        private lateinit var locationPointDao: LocationPointDao

        fun initialize(context: Context) {
            val database: TransparentRunningDatabase = Room.databaseBuilder(
                context.applicationContext,
                TransparentRunningDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(MIGRATION_5_6)
                .addMigrations(MIGRATION_4_5)
                .addMigrations(MIGRATION_3_4)
                .build()

            workoutSessionDao = database.workoutSessionDao()
            locationPointDao = database.locationPointDao()
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocationPoint RENAME TO LocationPointTemp")
                database.execSQL("CREATE TABLE IF NOT EXISTS `LocationPoint` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `sessionId` INTEGER NOT NULL, `time` INTEGER NOT NULL, `elapsedTime` INTEGER NOT NULL, `roundedElapsedTime` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `altitude` REAL NULL, `speed` REAL NULL, `bearing` REAL NULL, `elapsedDistance` REAL NOT NULL, `horizontalAccuracy` REAL NULL, `verticalAccuracy` REAL NULL, `speedAccuracy` REAL NULL, `bearingAccuracy` REAL NULL, `isSimulated` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `WorkoutSession`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("INSERT INTO LocationPoint(`id`, `sessionId`, `time`, `elapsedTime`, `roundedElapsedTime`, `latitude`, `longitude`, `altitude`, `speed`, `elapsedDistance`, `isSimulated` ) SELECT `id`, `sessionId`, `time`, `elapsedTime`, `roundedElapsedTime`, `latitude`, `longitude`, `altitude`, `speed`, `elapsedDistance`, `isSimulated`  FROM LocationPointTemp")
                database.execSQL("DROP TABLE LocationPointTemp")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_LocationPoint_sessionId` ON `LocationPoint` (`sessionId`)")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocationPoint ADD gpsAccuracy REAL NOT NULL default 0")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocationPoint ADD roundedElapsedTime INTEGER NOT NULL default 0")
                database.execSQL("ALTER TABLE LocationPoint ADD isSimulated INTEGER NOT NULL default 0")
                database.execSQL(
                    "UPDATE LocationPoint\n" +
                            "SET roundedElapsedTime = CAST( ROUND(elapsedTime/1000.0,0)*1000 AS INT)"
                )

                val query = database.query("SELECT * FROM WorkoutSession")
                val columnIndex = query.getColumnIndex("id")
                while (query.moveToNext()) {
                    val id = query.getInt(columnIndex)
                    database.execSQL(
                        "WITH \tRECURSIVE MinMaxTable AS (\n" +
                                "  \t\t\tSELECT MAX(roundedElapsedTime) maxdate, MIN(roundedElapsedTime) mindate\n" +
                                "  \t\t\tFROM LocationPoint\n" +
                                "\t\t\tWHERE sessionId = ${id}\n" +
                                "  \t\t),\n" +
                                "\t\txValTable(xVal) AS (\n" +
                                "     \t\tSELECT mindate \n" +
                                "  \t\t\tFROM MinMaxTable\n" +
                                "     \t\t\tUNION ALL\n" +
                                "     \t\tSELECT xVal+1000\n" +
                                "    \t\tFROM xValTable xvt\n" +
                                "    \t\t\tJOIN MinMaxTable mmt ON xvt.xVal < mmt.maxdate\n" +
                                "  \t\t),\n" +
                                "\t\tfinalTable AS (\n" +
                                "\t\t\tSELECT *,\n" +
                                "\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT time\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minTime,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT time\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxTime,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT elapsedTime\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minElapsedTime,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT elapsedTime\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxElapsedTime,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT latitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minLatitude,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT latitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxLatitude,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT longitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minLongitude,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT longitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxLongitude,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT altitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minAltitude,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT altitude\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxAltitude,\n" +
                                "\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT speed\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minSpeed,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT speed\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxSpeed,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT elapsedDistance\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minElapsedDistance,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT elapsedDistance\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxElapsedDistance,\n" +
                                "\t\t\t\t\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT roundedElapsedTime\n" +
                                "\t\t\t\t\t\tFROM LocationPoint minLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal > minLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND minLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime DESC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS minRoundedElapsedTime,\n" +
                                "\t\t\t\tCASE\n" +
                                "\t\t\t\t\tWHEN id IS NOT NULL THEN NULL\n" +
                                "\t\t\t\t\tELSE (\n" +
                                "\t\t\t\t\t\tSELECT roundedElapsedTime\n" +
                                "\t\t\t\t\t\tFROM LocationPoint maxLp\n" +
                                "\t\t\t\t\t\tWHERE xvt.xVal < maxLp.roundedElapsedTime\n" +
                                "\t\t\t\t\t\t\tAND maxLp.sessionId = ${id}\n" +
                                "\t\t\t\t\t\tORDER BY roundedElapsedTime ASC\n" +
                                "\t\t\t\t\t\tLIMIT 1\n" +
                                "\t\t\t\t\t)\n" +
                                "\t\t\t\tEND AS maxRoundedElapsedTime\n" +
                                "\t\t\t\t\n" +
                                "\t\t\tFROM xValTable xvt\n" +
                                "  \t\t\t\tLEFT JOIN LocationPoint lp ON lp.roundedElapsedTime = xvt.xVal AND lp.sessionId = ${id}\n" +
                                "\t\t)\n" +
                                "INSERT INTO LocationPoint(sessionId, time, elapsedTime, latitude, longitude, altitude, speed, elapsedDistance, roundedElapsedTime, isSimulated)\n" +
                                "SELECT $id AS sessionId, \n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN time\n" +
                                "\t\tELSE CAST(minTime-(minTime-maxTime*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal) AS INT)\n" +
                                "\tEND AS time,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN elapsedTime\n" +
                                "\t\tELSE CAST(minElapsedTime-(minElapsedTime-maxElapsedTime*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal) AS INT)\n" +
                                "\tEND AS elapsedTime,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN latitude\n" +
                                "\t\tELSE minLatitude-(minLatitude-maxLatitude*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal)\n" +
                                "\tEND AS latitude,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN longitude\n" +
                                "\t\tELSE minLongitude-(minLongitude-maxLongitude*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal)\n" +
                                "\tEND AS longitude,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN altitude\n" +
                                "\t\tELSE minAltitude-(minAltitude-maxAltitude*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal)\n" +
                                "\tEND AS altitude,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN speed\n" +
                                "\t\tELSE minSpeed-(minSpeed-maxSpeed*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal)\n" +
                                "\tEND AS speed,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN elapsedDistance\n" +
                                "\t\tELSE minElapsedDistance-(minElapsedDistance-maxElapsedDistance*1.0)/(minRoundedElapsedTime-maxRoundedElapsedTime)*(minRoundedElapsedTime-xVal)\n" +
                                "\tEND AS elapsedDistance,\n" +
                                "\txVal AS roundedElapsedTime,\n" +
                                "\tCASE \n" +
                                "\t\tWHEN id IS NOT NULL THEN isSimulated\n" +
                                "\t\tELSE 1\n" +
                                "\tEND AS isSimulated\n" +
                                "FROM finalTable\n" +
                                "WHERE id IS NULL"
                    )

                }
            }
        }

        fun insertWorkoutSession(workoutSession: WorkoutSession): Long = workoutSessionDao.insert(workoutSession)
        fun insertLocationPoint(locationPoint: LocationPoint): Long = locationPointDao.insert(locationPoint)
        fun getWorkoutSessions(): LiveData<List<WorkoutSession>> = workoutSessionDao.getAllWorkoutSessions()
        fun getLocationPoints(workoutSessionId: Long): LiveData<List<LocationPoint>> =
            locationPointDao.getAllLocationPoints(workoutSessionId)

        fun updateDurationAndTime(workoutSessionId: Long) = workoutSessionDao.updateDurationAndTime(workoutSessionId)
    }
}
