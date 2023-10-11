//package de.joshua.audioStream.misc
//
//import java.nio.ByteBuffer
//
//
//class MemoryQueue @JvmOverloads constructor(capacity: Int = 2048) {
//    interface CopyDelegate<SRC, DST> {
//        fun copy(source: SRC, sourceOffset: Int, destination: DST, destinationOffset: Int, length: Int)
//    }
//
//    private var head = 0
//    private var tail = 0
//    private var length = 0
//    private var buffer: ByteArray
//    fun size(): Int {
//        return length
//    }
//
//    init {
//        buffer = ByteArray(capacity)
//    }
//
//    fun clear() {
//        head = 0
//        tail = 0
//        length = 0
//    }
//
//    fun buffer(): ByteArray {
//        return buffer
//    }
//
//    fun capacity(): Int {
//        return buffer.size
//    }
//
//    private fun ensureCapacity(capacity: Int) {
//        if (capacity > buffer.size) {
//            setCapacity(capacity + 2047 and 2047.inv())
//        }
//    }
//
//    private fun setCapacity(capacity: Int) {
//        val newBuffer = ByteArray(capacity)
//        if (length > 0) {
//            if (head < tail) {
//                System.arraycopy(buffer, head, newBuffer, 0, length)
//            } else {
//                System.arraycopy(buffer, head, newBuffer, 0, buffer.size - head)
//                System.arraycopy(buffer, 0, newBuffer, buffer.size - head, tail)
//            }
//        }
//        head = 0
//        tail = length
//        buffer = newBuffer
//    }
//
//    fun <T> enqueue(source: T, count: Int, copyFrom: CopyDelegate<T, ByteArray?>) {
//        if (count == 0) {
//            return
//        }
//        ensureCapacity(length + count)
//        if (head < tail) {
//            val rightLength = buffer.size - tail
//            if (rightLength >= count) {
//                copyFrom.copy(source, 0, buffer, tail, count)
//            } else {
//                copyFrom.copy(source, 0, buffer, tail, rightLength)
//                copyFrom.copy(source, rightLength, buffer, 0, count - rightLength)
//            }
//        } else {
//            copyFrom.copy(source, 0, buffer, tail, count)
//        }
//        tail = (tail + count) % buffer.size
//        length += count
//    }
//
//    fun enqueue(buffer: ByteArray, offset: Int, count: Int) {
//        enqueue<ByteArray>(buffer, count,
//            CopyDelegate<ByteArray, ByteArray> { src: ByteArray?, sOff: Int, dst: ByteArray?, dOff: Int, len: Int ->
//                System.arraycopy(
//                    src,
//                    sOff + offset,
//                    dst,
//                    dOff,
//                    len
//                )
//            })
//    }
//
//    fun enqueue(buffer: ByteBuffer, count: Int) {
//        if (buffer.hasArray()) {
//            enqueue(buffer.array(), buffer.arrayOffset(), count)
//        } else {
//            enqueue(buffer, count,
//                CopyDelegate<ByteBuffer, ByteArray> { src: ByteBuffer, sOff: Int, dst: ByteArray, dOff: Int, len: Int ->
//                    for (i in 0 until len) {
//                        dst[dOff + i] = src[sOff + i]
//                    }
//                })
//        }
//    }
//
//    fun <T> dequeue(destination: T, count: Int, copyTo: CopyDelegate<ByteArray?, T>): Int {
//        var count = count
//        if (count > length) {
//            count = length
//        }
//        if (count == 0) {
//            return 0
//        }
//        if (head < tail) {
//            copyTo.copy(buffer, head, destination, 0, count)
//        } else {
//            val rightLength = buffer.size - head
//            if (rightLength >= count) {
//                copyTo.copy(buffer, head, destination, 0, count)
//            } else {
//                copyTo.copy(buffer, head, destination, 0, rightLength)
//                copyTo.copy(buffer, 0, destination, rightLength, count - rightLength)
//            }
//        }
//        head = (head + count) % buffer.size
//        length -= count
//        if (length == 0) {
//            head = 0
//            tail = 0
//        }
//        return count
//    }
//
//    fun dequeue(buffer: ByteArray, offset: Int, count: Int): Int {
//        return dequeue<ByteArray>(buffer, count,
//            CopyDelegate<ByteArray, ByteArray> { src: ByteArray?, sOff: Int, dst: ByteArray?, dOff: Int, len: Int ->
//                if (dst != null) {
//                    System.arraycopy(src, sOff, dst, dOff + offset, len)
//                }
//            })
//    }
//
//    fun dequeue(buffer: ByteBuffer, count: Int): Int {
//        return if (buffer.hasArray()) {
//            dequeue(buffer.array(), buffer.arrayOffset(), count)
//        } else {
//            dequeue(buffer, count,
//                CopyDelegate<ByteArray, ByteBuffer> { src: ByteArray, sOff: Int, dst: ByteBuffer, dOff: Int, len: Int ->
//                    for (i in 0 until len) {
//                        dst.put(dOff + i, src[sOff + i])
//                    }
//                })
//        }
//    }
//
//    fun dequeue(count: Int): Int {
//        return dequeue<Any?>(null, count,
//            CopyDelegate<ByteArray, Any> { src: ByteArray?, sOff: Int, dst: Any?, dOff: Int, len: Int -> })
//    }
//}
//
