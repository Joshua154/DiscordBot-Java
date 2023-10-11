//package de.joshua.audioStream
//
//import de.joshua.audioStream.misc.MemoryQueue
//import jouvieje.bass.structures.HRECORD
//import net.dv8tion.jda.api.audio.AudioSendHandler
//import org.slf4j.LoggerFactory
//import java.io.Closeable
//import java.nio.ByteBuffer
//
//class SpeakHandler : AudioSendHandler, Closeable {
//    private val logger = LoggerFactory.getLogger(SpeakHandler::class.java)
//    val FRAME_MILLIS = 20
//    val MAX_LAG = 200
//    private val activeHandlers: List<SpeakHandler> = ArrayList()
//
//    private val memoryQueueLock = Any()
//    private var recordingDevice = 0
//    private var recordingStream: HRECORD? = null
//    private var memoryQueue: MemoryQueue? = null
//    private var buffer: ByteArray? = null
//
//    fun SpeakHandler() {
//        recordingDevice = -1
//        buffer =
//            ByteArray(AudioSendHandler.INPUT_FORMAT.channels * (AudioSendHandler.INPUT_FORMAT.sampleRate * (FRAME_MILLIS / 1000f)).toInt() * (AudioSendHandler.INPUT_FORMAT.sampleSizeInBits / 8))
//    }
//    override fun canProvide(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun provide20MsAudio(): ByteBuffer? {
//        var numBytesRead: Int
//        synchronized(memoryQueueLock) { numBytesRead = memoryQueue.dequeue(buffer, 0, buffer!!.size) }
//        return ByteBuffer.wrap(buffer, 0, numBytesRead)
//    }
//
//    override fun close() {
//        TODO("Not yet implemented")
//    }
//}