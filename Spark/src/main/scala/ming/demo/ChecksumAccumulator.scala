package ming.demo

class ChecksumAccumulator {

  private var sum = 0
  def add(b: Byte): Unit ={
    sum += b
  }
  def checksum(): Int = {
    ~(sum & 0xFF)
  }
  def add1(b: Byte) = sum += b
  def checksum1 = ~(sum & 0xFF)

}
