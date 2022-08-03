package de.thm.sbwl47.rush_b_vanilla.view.helper

import android.content.Context
import de.thm.tp.library.sqlite.TPSQLite

class DB {
    companion object {
        var conn: TPSQLite? = null
    }

    fun createConn(context: Context) {
        conn = TPSQLite.open(context, "database")
    }
}