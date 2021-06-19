package com.decard.dblibs.column

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PeerDao {

    @Insert
    fun addPeer(peerBean: PeerBean): Completable

    @Query("SELECT * FROM PEER")
    fun getPeers(): Observable<MutableList<PeerBean>>
}