package com.example.grpcclient.parkplace

import ParkPlaceServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import parkPlaceId
import parkingId
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ParkPlaceClient(private val channel: ManagedChannel) : Closeable {
    val stub = ParkPlaceServiceGrpcKt.ParkPlaceServiceCoroutineStub(channel)
    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 8980).usePlaintext().build()

    ParkPlaceClient(channel).use {
        println(it.stub.getParkPlace(parkPlaceId { id = 1 }))
        println(it.stub.listParkPlace(parkingId { id = 1 }).toList())
        println(it.stub.listParkPlaces(flowOf(parkPlaceId { id = 1 }, parkPlaceId { id = 2 })).toList())
    }
}
