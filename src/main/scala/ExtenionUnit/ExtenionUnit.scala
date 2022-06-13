

package ExtenionUnit

import chisel3._
import chisel3.util._
import config.ExtensionCases._


class ExtenionUnit extends Module
{
  val io = IO(new Bundle{
    val ext_type   = Input(UInt(3.W))
    val instr      = Input(UInt(32.W))
    val ext_imm    = Output(UInt(32.W))
  })

  val instr = io.instr

  io.ext_imm := 0.U(32.W)

  switch(io.ext_type){
    is(id)    { io.ext_imm := Cat(Fill(18, instr(31)), instr(31), instr(7), instr(30,25), instr(11,8), 0.U(2.W))}    // in case of id - branch target
    is(jal)   { io.ext_imm := Cat(Fill(10, instr(31)), instr(31), instr(19,12), instr(20), instr(30,21), 0.U(2.W))}  // in case of jal
    is(jalr)  { io.ext_imm := Cat(Fill(18, instr(31)), instr(31,20), 0.U(2.W))}                                      // in case of jalr
    is(auipc) { io.ext_imm := Cat(instr(31,12), 0.U(12.W))}                                                          // in case of auipc
    is(store) { io.ext_imm := Cat(Fill(20, instr(31)), instr(31,25), instr(4,0))}                                    // in case store
    is(i_type){ io.ext_imm := Cat(Fill(20, instr(31)), instr(31,20))}                                                // in case of ldr or i type ALU
  }
}
