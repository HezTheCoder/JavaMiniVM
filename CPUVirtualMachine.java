package vnocs_emu;

import java.io.File;
import java.io.FileInputStream;

public class CPUVirtualMachine 
{
	public static final int MEM_SIZE = 8388608;
	public static final int NUM_GENERAL_REGS = 16;
	
	public static final int REG_XQA = 0;
	public static final int REG_XQB = 1;
	public static final int REG_XQC = 2;
	public static final int REG_XQD = 3;
	public static final int REG_XQE = 4;
	public static final int REG_XQF = 5;
	public static final int REG_XQG = 6;
	public static final int REG_XQH = 7;
	public static final int REG_PTR0 = 8;
	public static final int REG_PTR1 = 9;
	public static final int REG_XQI = 8;
	public static final int REG_XQJ = 9;
	public static final int REG_PTR2 = 10;
	public static final int REG_PTR3 = 11;
	public static final int REG_XQK = 10;
	public static final int REG_XQL = 11;
	public static final int REG_TEST = 15;
	public static final int REG_SP = 12;
	public static final int REG_FP = 13;
	public static final int REG_LK = 14;
	
	public static final int REG_SEG_RNN0 = 0;
	public static final int REG_SEG_RNN1 = 1;
	public static final int REG_SEG_RNN2 = 2;
	public static final int REG_SEG_RNN3 = 3;
	public static final int REG_SEG_RNN4 = 4;
	public static final int REG_SEG_RNN5 = 5;
	public static final int REG_SEG_RNN6 = 6;
	public static final int REG_SEG_RNN7 = 7;
	public static final int REG_SEG_RNX0 = 8;
	public static final int REG_SEG_RNX1 = 9;
	public static final int REG_SEG_RNX2 = 10;
	public static final int REG_SEG_RNX3 = 11;
	public static final int REG_SEG_RNX4 = 12;
	public static final int REG_SEG_RNX5 = 13;
	public static final int REG_SEG_RNX6 = 14;
	public static final int REG_SEG_RNX7 = 15;
	public static final int REG_SEG_RWN0 = 16;
	public static final int REG_SEG_RWN1 = 17;
	public static final int REG_SEG_RWN2 = 18;
	public static final int REG_SEG_RWN3 = 19;
	public static final int REG_SEG_RWN4 = 20;
	public static final int REG_SEG_RWN5 = 21;
	public static final int REG_SEG_RWN6 = 22;
	public static final int REG_SEG_RWN7 = 23;
	public static final int REG_SEG_RWX0 = 24;
	public static final int REG_SEG_RWX1 = 25;
	public static final int REG_SEG_RWX2 = 26;
	public static final int REG_SEG_RWX3 = 27;
	public static final int REG_SEG_RWX4 = 28;
	public static final int REG_SEG_RWX5 = 29;
	public static final int REG_SEG_RWX6 = 30;
	public static final int REG_SEG_RWX7 = 31;
	public static final int NUM_SEGMENT_REGS = 32;
	
	public static final int ADDR_MODE_PTR0_REL = 0;
	public static final int ADDR_MODE_PTR1_REL = 1;
	public static final int ADDR_MODE_PTR2_REL = 2;
	public static final int ADDR_MODE_PTR3_REL = 3;
	public static final int ADDR_MODE_SP_REL = 4;
	public static final int ADDR_MODE_FP_REL = 5;
	public static final int ADDR_MODE_PC_REL = 6;
	public static final int ADDR_MODE_ABS = 7;
	
	public CPUVirtualMachine()
	{
		memory = new byte[MEM_SIZE];
		regs = new long[NUM_GENERAL_REGS];
		segregs = new long[NUM_SEGMENT_REGS];
	}
	
	public void Reset()
	{
		regs[REG_XQA] = 0;
		regs[REG_XQB] = 0;
		regs[REG_XQC] = 0;
		regs[REG_XQD] = 0;
		regs[REG_XQE] = 0;
		regs[REG_XQF] = 0;
		regs[REG_XQG] = 0;
		regs[REG_XQH] = 0;
		regs[REG_PTR2] = 0;
		regs[REG_PTR3] = 0;
		regs[REG_TEST] = 0;
		regs[REG_SP] = 0;
		regs[REG_FP] = 0;
		regs[REG_LK] = 0;
		regs[REG_PTR0] = 0;
		regs[REG_PTR1] = 0;
		
		segregs[0] = 0;
		segregs[1] = 0;
		segregs[2] = 0;
		segregs[3] = 0;
		segregs[4] = 0;
		segregs[5] = 0;
		segregs[6] = 0;
		segregs[7] = 0;
		segregs[8] = 0;
		segregs[9] = 0;
		segregs[10] = 0;
		segregs[11] = 0;
		segregs[12] = 0;
		segregs[13] = 0;
		segregs[14] = 0;
		segregs[15] = 0;
		segregs[16] = 0;
		segregs[17] = 0;
		segregs[18] = 0;
		segregs[19] = 0;
		segregs[20] = 0;
		segregs[21] = 0;
		segregs[22] = 0;
		segregs[23] = 0;
		segregs[24] = 0;
		segregs[25] = 0;
		segregs[26] = 0;
		segregs[27] = 0;
		segregs[28] = 0;
		segregs[29] = 0;
		segregs[30] = 0;
		segregs[31] = 0;
		
		//always start execution from 0.
		pc = 0;
	}
	
	public void SetSegmentRegister(long nSegAddr, int nSegReg)
	{
		segregs[nSegReg] = nSegAddr;
	}
	
	public void LoadImageFromFile(String strBinaryPath) throws Exception
	{
		File imageFile = new File(strBinaryPath);
		FileInputStream imageInput = new FileInputStream(imageFile);
		
		imageInput.read(memory, 0, MEM_SIZE);
		
		imageInput.close();
	}
	
	public static int SignExtend8To32(int nOperand)
	{
		//8 bit integer => sign bit is bit 7
		if((nOperand & 0x80) == 0x80)
		{
			return (0xFFFFFF00 | nOperand);
		}
		return nOperand;		
	}
	public static long SignExtend8To64(int nOperand)
	{
		long out = (long)nOperand;
		
		//8 bit integer => sign bit is bit 7
		if((out & 0x80L) == 0x80L)
		{
			return (0xFFFFFFFFFFFFFF00L | out);
		}
		return out;		
	}
	public static long SignExtend32To64(int nOperand)
	{
		long out = (long)nOperand;
		
		//32 bit integer => sign bit is bit 31
		if((out & 0x80000000L) == 0x80000000L)
		{
			return (0xFFFFFFFF00000000L | out);
		}
		return out;				
	}
	public static int SignExtend20To32(int nOperand)
	{
		if((nOperand & 0x80000) == 0x80000)
		{
			return (0xFFF00000 | nOperand);
		}
		return nOperand;
	}
	public static long SignExtend20To64(int nOperand)
	{
		if((nOperand & 0x80000) == 0x80000)
		{
			return (0xFFFFFFFFFFF00000L | nOperand);
		}
		return (long)nOperand;
	}
	
	//if return value is < 0, this is an error code.
	public int SingleStep()
	{
		/* #define OPC1B_REG_A (instrBytes[1] >> 4)
#define OPC1B_REG_B (instrBytes[1] & 0x0F)
#define OPC1B_REG_C (instrBytes[2] >> 4)

#define OPC2B_REG_A (instrBytes[2] >> 4)
#define OPC2B_REG_B (instrBytes[2] & 0x0F)
#define OPC2B_REG_C (instrBytes[3] >> 4)

//#define INST_MEM_OFFSET (opc & 0xFFFFF)

//20-bit signed displacement
#define OPC1B_MEM_DISP ((instr & 0x80000) ? (~(instr & 0xFFFFF) + 1) : (instr & 0xFFFFF))

//20-bit immediate
#define OPC1B_IMM20 ((instr & 0x80000) ? (~(instr & 0xFFFFF) + 1) : (instr & 0xFFFFF))

//indicates which bit to set on carry
#define OPC1B_CARRY_BIT_OUT (instr & 0x3F)
#define OPC2B_CARRY_BIT_OUT (instr & 0x3F)

//indicates which bit to use as carry bit
#define OPC1B_CARRY_BIT_IN ((instr & 0xFC0) >> 6)
#define OPC2B_CARRY_BIT_IN ((instr & 0xFC0) >> 6)

//bit selectors for SETTB/CLTB
#define OPC1B_TEST_BITSEL_0 ((instr & 0x00FC0000) >> 18)
#define OPC1B_TEST_BITSEL_1 ((instr & 0x0003F000) >> 12)
#define OPC1B_TEST_BITSEL_2 ((instr & 0xFC0) >> 6)
#define OPC1B_TEST_BITSEL_3 (instr & 0x3F)

//bit count to compare hamming weight of $TEST with for JTBCx instructions
#define OPC1B_TEST_BITCOUNT ((instr & 0x3F00000) >> 20)
		
		*/
		//some temporaries for data movement and arithmetic
		byte tb0 = 0, tb1 = 0, tb2 = 0, tb3 = 0;
		int ti0 = 0, ti1 = 0, ti2 = 0, ti3 = 0;
		long tl0 = 0, tl1 = 0, tl2 = 0, tl3 = 0;
		float tf0 = 0.0f, tf1 = 0.0f, tf2 = 0.0f, tf3 = 0.0f;
		
		//read next instruction dword
		//long instrAddr = DATA_PHYADDR(0, ADDR_MODE_PC_REL);
		//int instr = ReadDword(instrAddr);
		int instr = ReadDword(pc);
		
		//advance pc
		pc += 4;
		
		//some instructions have a source and destination register
		//(0x00AB0000)
		int regOperandA = ((instr >> 20) & 0xF);
		int regOperandB = ((instr >> 16) & 0xF);
		int regOperandC = ((instr >> 12) & 0xF);
		int opc2BRegOperandA = ((instr >> 12) & 0xF);
		int opc2BRegOperandB = ((instr >> 8) & 0xF);
		int opc2BRegOperandC = (instr & 0xF);
		
		//some instructions have a signed offset
		//(0x000FFFFF)
		int offset = SignExtend20To32((instr & 0xFFFFF));
		int imm20 = (instr & 0xFFFFF);
		
		//carry-in and carry-out bit selectors
		int carryBitOut = (instr & 0x3F);

		//indicates which bit to use as carry bit
		int carryBitIn = ((instr & 0xFC0) >> 6);
		
		//bit selectors for SETTB/CLTB
		int testBitsel0 = ((instr & 0x00FC0000) >> 18);
		int testBitsel1 = ((instr & 0x0003F000) >> 12);
		int testBitsel2 = ((instr & 0xFC0) >> 6);
		int testBitsel3 = (instr & 0x3F);
		
		//bit count to compare hamming weight of $TEST with for JTBCx instructions
		int testBitcount = ((instr & 0x3F00000) >> 20);
		
		//opcode is in highest byte (0xFF000000)
		switch((instr & 0xFF000000))
		{
		case CPUOpcodes.OPC1B_NOP: 
			break;
		case CPUOpcodes.OPC1B_LEA_PTR0_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_PTR1_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_PTR2_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_PTR3_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_SP_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_SP_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_FP_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_FP_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_PC_REL: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_PC_REL);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LEA_ABS: 
			tl0 = DATA_PHYADDR(offset, ADDR_MODE_ABS);
			regs[regOperandA] = tl0;
			break;
		case CPUOpcodes.OPC1B_LDR_Q_PTR0_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_PTR1_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_PTR2_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_PTR3_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_SP_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_FP_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_FP_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_PC_REL: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_PC_REL));
			break;
		case CPUOpcodes.OPC1B_LDR_Q_ABS: 
			regs[regOperandA] = ReadQword(DATA_PHYADDR(offset, ADDR_MODE_ABS));
			break;
		case CPUOpcodes.OPC1B_LDR_D_PTR0_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_PTR1_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_PTR2_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_PTR3_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_SP_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_SP_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_FP_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_FP_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_PC_REL: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_PC_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_D_ABS: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(offset, ADDR_MODE_ABS)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_PTR0_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_PTR1_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_PTR2_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_PTR3_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_SP_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_SP_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_FP_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_FP_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_PC_REL: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_PC_REL)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDR_B_ABS: 
			WriteByteReg(ReadByte(DATA_PHYADDR(offset, ADDR_MODE_ABS)), regOperandA);
			break;
		case CPUOpcodes.OPC1B_STR_Q_PTR0_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_PTR1_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_PTR2_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_PTR3_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_SP_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_FP_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_FP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_PC_REL: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_PC_REL));
			break;
		case CPUOpcodes.OPC1B_STR_Q_ABS: 
			WriteQword(regs[regOperandA], DATA_PHYADDR(offset, ADDR_MODE_ABS));
			break;
		case CPUOpcodes.OPC1B_STR_D_PTR0_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_PTR1_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_PTR2_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_PTR3_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_SP_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_FP_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_FP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_PC_REL: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PC_REL));
			break;
		case CPUOpcodes.OPC1B_STR_D_ABS: 
			WriteDword(ReadDwordReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_ABS));
			break;
		case CPUOpcodes.OPC1B_STR_B_PTR0_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR0_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_PTR1_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR1_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_PTR2_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR2_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_PTR3_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PTR3_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_SP_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_FP_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_FP_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_PC_REL: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_PC_REL));
			break;
		case CPUOpcodes.OPC1B_STR_B_ABS: 
			WriteByte(ReadByteReg(regOperandA), DATA_PHYADDR(offset, ADDR_MODE_ABS));
			break;
			
		case CPUOpcodes.OPC1B_ASXI_D:
			WriteDwordReg(ReadDwordReg(regOperandA) + SignExtend20To32(imm20), regOperandA);
			break;
		case CPUOpcodes.OPC1B_ASXI_Q:
			regs[regOperandA] = regs[regOperandA] + SignExtend20To64(imm20);
			break;
		case CPUOpcodes.OPC1B_AZXI_D:
			WriteDwordReg(ReadDwordReg(regOperandA) + (instr & 0xFFFFF), regOperandA);
			break;
		case CPUOpcodes.OPC1B_AZXI_Q:
			regs[regOperandA] = regs[regOperandA] + (instr & 0xFFFFF);
			break;
			
		case CPUOpcodes.OPC1B_MVR_B: 
			WriteByteReg(ReadByteReg(regOperandA), regOperandB);
			WriteByteReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVR_D: 
			WriteDwordReg(ReadDwordReg(regOperandA), regOperandB);
			WriteDwordReg(0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVR_Q: 
			regs[regOperandB] = regs[regOperandA];
			regs[regOperandA] = 0;
			break;
		case CPUOpcodes.OPC1B_CYR_B: 
			WriteByteReg(ReadByteReg(regOperandA), regOperandB);
			break;
		case CPUOpcodes.OPC1B_CYR_D: 
			WriteDwordReg(ReadDwordReg(regOperandA), regOperandB);
			break;
		case CPUOpcodes.OPC1B_CYR_Q: 
			regs[regOperandB] = regs[regOperandA];
			break;
		case CPUOpcodes.OPC1B_XCGR_B: 
			tb0 = ReadByteReg(regOperandA);
			WriteByteReg(ReadByteReg(regOperandB), regOperandA);
			WriteByteReg(tb0, regOperandB);
			break;
		case CPUOpcodes.OPC1B_XCGR_D: 
			tb0 = ReadByteReg(regOperandA);
			WriteByteReg(ReadByteReg(regOperandB), regOperandA);
			WriteByteReg(tb0, regOperandB);
			break;
		case CPUOpcodes.OPC1B_XCGR_Q:
			tl0 = regs[regOperandA];
			regs[regOperandA] = regs[regOperandB];
			regs[regOperandB] = tl0;
			break;
		case CPUOpcodes.OPC1B_MVRSE_BD: 
			WriteDwordReg(SignExtend8To32(ReadByteReg(regOperandA)), regOperandB);
			WriteByteReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVRSE_BQ: 
			regs[regOperandB] = SignExtend8To64(ReadByteReg(regOperandA));
			WriteByteReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVRSE_DQ: 
			regs[regOperandB] = SignExtend32To64(ReadDwordReg(regOperandA));
			WriteDwordReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVRZE_BD: 
			WriteDwordReg((int)ReadByteReg(regOperandA), regOperandB);
			WriteByteReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVRZE_BQ: 
			regs[regOperandB] = (long)ReadByteReg(regOperandA);
			WriteByteReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_MVRZE_DQ: 
			regs[regOperandB] = (long)ReadDwordReg(regOperandA);
			WriteDwordReg((byte)0, regOperandA);
			break;
		case CPUOpcodes.OPC1B_CYRZE_BD: 
			WriteDwordReg((int)ReadByteReg(regOperandA), regOperandB);
			break;
		case CPUOpcodes.OPC1B_CYRZE_BQ: 
			regs[regOperandB] = (long)ReadByteReg(regOperandA);
			break;
		case CPUOpcodes.OPC1B_CYRZE_DQ: 
			regs[regOperandB] = (long)ReadDwordReg(regOperandA);
			break;
		case CPUOpcodes.OPC1B_CYRSE_BD: 
			WriteDwordReg(SignExtend8To32(ReadByteReg(regOperandA)), regOperandB);
			break;
		case CPUOpcodes.OPC1B_CYRSE_BQ: 
			regs[regOperandB] = SignExtend8To64(ReadByteReg(regOperandA));
			break;
		case CPUOpcodes.OPC1B_CYRSE_DQ: 
			regs[regOperandB] = SignExtend32To64(ReadDwordReg(regOperandA));
			break;
		case CPUOpcodes.OPC1B_LDIZE_D: 
			WriteDwordReg(imm20, regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDIZE_Q: 
			regs[regOperandA] = (long)imm20;
			break;
		case CPUOpcodes.OPC1B_LDISE_D: 
			WriteDwordReg(SignExtend20To32(imm20), regOperandA);
			break;
		case CPUOpcodes.OPC1B_LDISE_Q: 
			regs[regOperandA] = SignExtend20To64(imm20);
			break;
		case CPUOpcodes.OPC1B_SETTB: 
			regs[REG_TEST] |= ((1L << testBitsel0) | (1L << testBitsel1) | (1L << testBitsel2) | (1L << testBitsel3));
			break;
		case CPUOpcodes.OPC1B_CLTB: 
			regs[REG_TEST] |= ((~(1L << testBitsel0)) & (~(1L << testBitsel1)) & (~(1L << testBitsel2)) & (~(1L << testBitsel3)));
			break;
		case CPUOpcodes.OPC1B_PSHIZE_D: 
			regs[REG_SP] -= 4;
			WriteDword(imm20, DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_PSHIZE_Q: 
			regs[REG_SP] -= 8;
			WriteQword(imm20, DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_PSHISE_D: 
			regs[REG_SP] -= 4;
			WriteDword(SignExtend20To32(imm20), DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_PSHISE_Q: 
			regs[REG_SP] -= 8;
			WriteQword(SignExtend20To64(imm20), DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC1B_JU: 
			pc += SignExtend20To64(imm20);
			break;
		case CPUOpcodes.OPC1B_JTZ: 
			if(regs[REG_TEST] == 0) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTNZ: 
			if(regs[REG_TEST] != 0) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTS: 
			if((regs[REG_TEST] & 0x8000000000000000L) != 0) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTNS: 
			if((regs[REG_TEST] & 0x8000000000000000L) == 0) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCG_0:
		case CPUOpcodes.OPC1B_JTBCG_1:
		case CPUOpcodes.OPC1B_JTBCG_2:
		case CPUOpcodes.OPC1B_JTBCG_3: 
			if(Long.bitCount(regs[REG_TEST]) > testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCL_0:
		case CPUOpcodes.OPC1B_JTBCL_1:
		case CPUOpcodes.OPC1B_JTBCL_2:
		case CPUOpcodes.OPC1B_JTBCL_3:
			if(Long.bitCount(regs[REG_TEST]) < testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCE_0:
		case CPUOpcodes.OPC1B_JTBCE_1:
		case CPUOpcodes.OPC1B_JTBCE_2:
		case CPUOpcodes.OPC1B_JTBCE_3: 
			if(Long.bitCount(regs[REG_TEST]) == testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCNE_0:
		case CPUOpcodes.OPC1B_JTBCNE_1:
		case CPUOpcodes.OPC1B_JTBCNE_2:
		case CPUOpcodes.OPC1B_JTBCNE_3:
			if(Long.bitCount(regs[REG_TEST]) != testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCGE_0:
		case CPUOpcodes.OPC1B_JTBCGE_1:
		case CPUOpcodes.OPC1B_JTBCGE_2:
		case CPUOpcodes.OPC1B_JTBCGE_3: 
			if(Long.bitCount(regs[REG_TEST]) >= testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JTBCLE_0:
		case CPUOpcodes.OPC1B_JTBCLE_1:
		case CPUOpcodes.OPC1B_JTBCLE_2:
		case CPUOpcodes.OPC1B_JTBCLE_3: 
			if(Long.bitCount(regs[REG_TEST]) <= testBitcount) { pc += SignExtend20To64(imm20); }
			break;
		case CPUOpcodes.OPC1B_JUF_PTR0_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR0_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_PTR1_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR1_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_PTR2_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR2_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_PTR3_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR3_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_SP_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_SP_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_FP_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_FP_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_PC_REL: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PC_REL);
			break;
		case CPUOpcodes.OPC1B_JUF_ABS: 
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_ABS);
			break;			
		case CPUOpcodes.OPC1B_ESR_PTR0_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR0_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_PTR1_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR1_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_PTR2_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR2_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_PTR3_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PTR3_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_SP_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_SP_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_FP_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_FP_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_PC_REL: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_PC_REL);
			break;
		case CPUOpcodes.OPC1B_ESR_ABS: 
			regs[REG_LK] = pc;
			pc = DATA_PHYADDR(SignExtend20To32(imm20), ADDR_MODE_ABS);
			break;
		case 0xF0000000:
			//two-byte opcode
			return ExecuteInstOpc2B(instr);
		default:
			System.out.printf("*** STOP: INVALID INSTRUCTION *** (attempted to execute invalid opcode 0x%08X at 0x%016X)\n",
					instr, pc);
			PrintRegisterDump();
			return -2;
		}
		
		return 1;
	}
	
	private int ExecuteInstOpc2B(int nInstr)
	{
		//some temporaries for data movement and arithmetic
		byte tb0 = 0, tb1 = 0, tb2 = 0, tb3 = 0;
		int ti0 = 0, ti1 = 0, ti2 = 0, ti3 = 0;
		long tl0 = 0, tl1 = 0, tl2 = 0, tl3 = 0;
		float tf0 = 0.0f, tf1 = 0.0f, tf2 = 0.0f, tf3 = 0.0f;
		
		//some instructions have a source and destination register
		//(0x00AB0000)
		int opc2BRegOperandA = ((nInstr >> 12) & 0xF);
		int opc2BRegOperandB = ((nInstr >> 8) & 0xF);
		int opc2BRegOperandC = ((nInstr >> 4) & 0xF);
		
		//some nInstructions have a signed offset
		//(0x000FFFFF)
		int offset = SignExtend20To32((nInstr & 0xFFFFF));
		int imm20 = (nInstr & 0xFFFFF);
		
		//carry-in and carry-out bit selectors
		int carryBitOut = (nInstr & 0x3F);

		//indicates which bit to use as carry bit
		int carryBitIn = ((nInstr & 0xFC0) >> 6);
		
		switch((nInstr & 0xFFFF0000))
		{
		case CPUOpcodes.OPC2B_LDI_B: 
			WriteByteReg((byte)(nInstr & 0xFF), opc2BRegOperandA);
			break;
		case CPUOpcodes.OPC2B_IADD_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) + ReadDwordReg(opc2BRegOperandB),
				opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_IADD_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] + regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_ISUB_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) - ReadDwordReg(opc2BRegOperandB),
				opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_ISUB_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] - regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_IMUL_D: 
			int x = ReadDwordReg(opc2BRegOperandA);
			int y = ReadDwordReg(opc2BRegOperandB);
			System.out.printf("DEBUG imul: x = %d, y = %d\n", x, y);
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) * ReadDwordReg(opc2BRegOperandB), 
				opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_IMUL_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] * regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_IDIV_D:
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) * ReadDwordReg(opc2BRegOperandB), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_IDIV_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] / regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_IMOD_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) % ReadDwordReg(opc2BRegOperandB), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_IMOD_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] % regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_INEG_D: 
			WriteDwordReg((~ReadDwordReg(opc2BRegOperandA)) + 1, opc2BRegOperandB);
			break;
		case CPUOpcodes.OPC2B_INEG_Q: 
			regs[opc2BRegOperandB] = (~regs[opc2BRegOperandA]) + 1;
			break;
		case CPUOpcodes.OPC2B_FADD_D: 
			WriteDwordReg(Float.floatToIntBits(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) + Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB))), 
					opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_FADD_Q: 
			regs[opc2BRegOperandC] = 
					Double.doubleToLongBits(Double.longBitsToDouble(regs[opc2BRegOperandA]) + Double.longBitsToDouble(regs[opc2BRegOperandB]));
			break;
		case CPUOpcodes.OPC2B_FSUB_D: 
			WriteDwordReg(Float.floatToIntBits(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) - Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB))), 
					opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_FSUB_Q: 
			regs[opc2BRegOperandC] = 
				Double.doubleToLongBits(Double.longBitsToDouble(regs[opc2BRegOperandA]) - Double.longBitsToDouble(regs[opc2BRegOperandB]));
			break;
		case CPUOpcodes.OPC2B_FMUL_D: 
			WriteDwordReg(Float.floatToIntBits(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) * Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB))), 
					opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_FMUL_Q: 
			regs[opc2BRegOperandC] = 
				Double.doubleToLongBits(Double.longBitsToDouble(regs[opc2BRegOperandA]) * Double.longBitsToDouble(regs[opc2BRegOperandB]));
			break;
		case CPUOpcodes.OPC2B_FDIV_D: 
			WriteDwordReg(Float.floatToIntBits(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) / Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB))), 
					opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_FDIV_Q: 
			regs[opc2BRegOperandC] = 
			Double.doubleToLongBits(Double.longBitsToDouble(regs[opc2BRegOperandA]) / Double.longBitsToDouble(regs[opc2BRegOperandB]));
			break;
		//fixed-point arithmetic not yet implemented
		/* case CPUOpcodes.OPC2B_QADD_D: break;
		case CPUOpcodes.OPC2B_QADD_Q: break;
		case CPUOpcodes.OPC2B_QSUB_D: break;
		case CPUOpcodes.OPC2B_QSUB_Q: break;
		case CPUOpcodes.OPC2B_QMUL_D: break;
		case CPUOpcodes.OPC2B_QMUL_Q: break;
		case CPUOpcodes.OPC2B_QDIV_D: break;
		case CPUOpcodes.OPC2B_QDIV_Q: break; */
		case CPUOpcodes.OPC2B_BWN_B: 
			WriteByteReg((byte)~ReadByteReg(opc2BRegOperandA), opc2BRegOperandB);
			break;
		case CPUOpcodes.OPC2B_BWN_D: 
			WriteDwordReg(~ReadDwordReg(opc2BRegOperandA), opc2BRegOperandB);
			break;
		case CPUOpcodes.OPC2B_BWN_Q: 
			regs[opc2BRegOperandA] = ~regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_BWA_B: 
			WriteByteReg((byte)(ReadByteReg(opc2BRegOperandA) & ReadByteReg(opc2BRegOperandB)), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWA_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) & ReadDwordReg(opc2BRegOperandB), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWA_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] & regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_BWO_B: 
			WriteByteReg((byte)(ReadByteReg(opc2BRegOperandA) | ReadByteReg(opc2BRegOperandB)), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWO_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) | ReadDwordReg(opc2BRegOperandB), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWO_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] | regs[opc2BRegOperandB];
			break;
		case CPUOpcodes.OPC2B_BWX_B: 
			WriteByteReg((byte)(ReadByteReg(opc2BRegOperandA) ^ ReadByteReg(opc2BRegOperandB)), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWX_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) ^ ReadDwordReg(opc2BRegOperandB), opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_BWX_Q: 
			regs[opc2BRegOperandC] = regs[opc2BRegOperandA] ^ regs[opc2BRegOperandB];
			break;
		/* case CPUOpcodes.OPC2B_LSHO_B: break;
		case CPUOpcodes.OPC2B_LSHO_D: break;
		case CPUOpcodes.OPC2B_LSHO_Q: break; */
		/*case CPUOpcodes.OPC2B_LSH_B: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) << ReadDwordReg(opc2BRegOperandB),
				opc2BRegOperandC);
			break; */
		case CPUOpcodes.OPC2B_LSH_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) << ReadDwordReg(opc2BRegOperandB),
				opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_LSH_Q: 
			regs[opc2BRegOperandC] = (regs[opc2BRegOperandA] << regs[opc2BRegOperandB]);
			break;
		/* case CPUOpcodes.OPC2B_LSHC_B: break;
		case CPUOpcodes.OPC2B_LSHC_D: break;
		case CPUOpcodes.OPC2B_LSHC_Q: break;
		case CPUOpcodes.OPC2B_RSHO_B: break;
		case CPUOpcodes.OPC2B_RSHO_D: break;
		case CPUOpcodes.OPC2B_RSHO_Q: break; */
		/*case CPUOpcodes.OPC2B_RSH_B: break; */
		case CPUOpcodes.OPC2B_RSH_D: 
			WriteDwordReg(ReadDwordReg(opc2BRegOperandA) >> ReadDwordReg(opc2BRegOperandB),
				opc2BRegOperandC);
			break;
		case CPUOpcodes.OPC2B_RSH_Q: 
			regs[opc2BRegOperandC] = (regs[opc2BRegOperandA] >> regs[opc2BRegOperandB]);
			break;
		/*case CPUOpcodes.OPC2B_RSHC_B: break;
		case CPUOpcodes.OPC2B_RSHC_D: break;
		case CPUOpcodes.OPC2B_RSHC_Q: break; */
		/* case CPUOpcodes.OPC2B_RASH_B: break; */
		/*case CPUOpcodes.OPC2B_RASH_D: 
			
			break;
		case CPUOpcodes.OPC2B_RASH_Q: break; */
		case CPUOpcodes.OPC2B_TZ_B: 
			if(ReadByteReg(opc2BRegOperandA) == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TZ_D: 
			if(ReadDwordReg(opc2BRegOperandA) == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TZ_Q: 
			if(regs[opc2BRegOperandA] == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNZ_B: 
			if(ReadByteReg(opc2BRegOperandA) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNZ_D: 
			if(ReadDwordReg(opc2BRegOperandA) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNZ_Q: 
			if(regs[opc2BRegOperandA] != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TS_B: 
			if((ReadByteReg(opc2BRegOperandA) & 0x80) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TS_D:
			if((ReadDwordReg(opc2BRegOperandA) & 0x80000000) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TS_Q: 
			if((regs[opc2BRegOperandA] & 0x8000000000000000L) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNS_B: 
			if((ReadByteReg(opc2BRegOperandA) & 0x80) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNS_D:
			if((ReadDwordReg(opc2BRegOperandA) & 0x80000000) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_TNS_Q: 
			if((regs[opc2BRegOperandA] & 0x8000000000000000L) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPA_B: 
			if((ReadByteReg(opc2BRegOperandA) & ReadByteReg(opc2BRegOperandB)) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPA_D: 
			if((ReadDwordReg(opc2BRegOperandA) & ReadDwordReg(opc2BRegOperandB)) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPA_Q: 
			if((regs[opc2BRegOperandA] & regs[opc2BRegOperandB]) != 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPAZ_B: 
			if((ReadByteReg(opc2BRegOperandA) & ReadByteReg(opc2BRegOperandB)) == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPAZ_D: 
			if((ReadDwordReg(opc2BRegOperandA) & ReadDwordReg(opc2BRegOperandB)) == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPAZ_Q: 
			if((regs[opc2BRegOperandA] & regs[opc2BRegOperandB]) == 0) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPE_B: 
			if(ReadByteReg(opc2BRegOperandA) == ReadByteReg(opc2BRegOperandB)) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPE_D: 
			if(ReadDwordReg(opc2BRegOperandA) == ReadDwordReg(opc2BRegOperandB)) 
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPE_Q: 
			if(regs[opc2BRegOperandA] == regs[opc2BRegOperandB]) 
			{ 
				regs[REG_TEST] |= (1 << carryBitOut); 
			}
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPNE_B: 
			if(ReadByteReg(opc2BRegOperandA) != ReadByteReg(opc2BRegOperandB)) 
			{ 
				regs[REG_TEST] |= (1 << carryBitOut); 
			}
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPNE_D: 
			if(ReadDwordReg(opc2BRegOperandA) != ReadDwordReg(opc2BRegOperandB)) 
			{ 
				regs[REG_TEST] |= (1 << carryBitOut); 
			}
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPNE_Q: 
			if(regs[opc2BRegOperandA] != regs[opc2BRegOperandB]) 
			{ 
				regs[REG_TEST] |= (1 << carryBitOut); 
			}
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPL_B: 
			if(ReadByteReg(opc2BRegOperandA) < ReadByteReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPL_D: 
			if(ReadDwordReg(opc2BRegOperandA) < ReadDwordReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }		
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPL_Q: 
			if(regs[opc2BRegOperandA] < regs[opc2BRegOperandB])
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPLE_B: 
			if(ReadByteReg(opc2BRegOperandA) <= ReadByteReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPLE_D: 
			if(ReadDwordReg(opc2BRegOperandA) <= ReadDwordReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPLE_Q: 
			if(regs[opc2BRegOperandA] <= regs[opc2BRegOperandB])
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPG_B: 
			if(ReadByteReg(opc2BRegOperandA) > ReadByteReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPG_D: 
			if(ReadDwordReg(opc2BRegOperandA) > ReadDwordReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPG_Q: 
			if(regs[opc2BRegOperandA] > regs[opc2BRegOperandB])
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPGE_B: 
			if(ReadByteReg(opc2BRegOperandA) >= ReadByteReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPGE_D: 
			if(ReadDwordReg(opc2BRegOperandA) >= ReadDwordReg(opc2BRegOperandB))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_CPGE_Q: 
			if(regs[opc2BRegOperandA] >= regs[opc2BRegOperandB])
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		
		//unsigned compare not yet implemented
		/*case CPUOpcodes.OPC2B_UCPL_B: break;
		case CPUOpcodes.OPC2B_UCPL_D: break;
		case CPUOpcodes.OPC2B_UCPL_Q: break;
		case CPUOpcodes.OPC2B_UCPLE_B: break;
		case CPUOpcodes.OPC2B_UCPLE_D: break;
		case CPUOpcodes.OPC2B_UCPLE_Q: break;
		case CPUOpcodes.OPC2B_UCPG_B: break;
		case CPUOpcodes.OPC2B_UCPG_D: break;
		case CPUOpcodes.OPC2B_UCPG_Q: break;
		case CPUOpcodes.OPC2B_UCPGE_B: break;
		case CPUOpcodes.OPC2B_UCPGE_D: break;
		case CPUOpcodes.OPC2B_UCPGE_Q: break; */
		
		case CPUOpcodes.OPC2B_FCPL_D: 
			if(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) < Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB)))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPL_Q: 
			if(Double.longBitsToDouble(regs[opc2BRegOperandA]) < Double.longBitsToDouble(regs[opc2BRegOperandB]))
			{ regs[REG_TEST] |= (1 << carryBitOut); }		
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPLE_D: 
			if(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) <= Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB)))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPLE_Q: 
			if(Double.longBitsToDouble(regs[opc2BRegOperandA]) <= Double.longBitsToDouble(regs[opc2BRegOperandB]))
			{ regs[REG_TEST] |= (1 << carryBitOut); }	
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPG_D: 
			if(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) > Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB)))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPG_Q: 
			if(Double.longBitsToDouble(regs[opc2BRegOperandA]) > Double.longBitsToDouble(regs[opc2BRegOperandB]))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPGE_D: 
			if(Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandA)) >= Float.intBitsToFloat(ReadDwordReg(opc2BRegOperandB)))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_FCPGE_Q: 
			if(Double.longBitsToDouble(regs[opc2BRegOperandA]) >= Double.longBitsToDouble(regs[opc2BRegOperandB]))
			{ regs[REG_TEST] |= (1 << carryBitOut); }
			else { regs[REG_TEST] &= ~(1 << carryBitOut); }
			break;
		case CPUOpcodes.OPC2B_PSH_B: 
			regs[REG_SP] -= 1;
			WriteByte(ReadByteReg(opc2BRegOperandA), DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break;
		case CPUOpcodes.OPC2B_PSH_D: 
			regs[REG_SP] -= 4;
			WriteDword(ReadDwordReg(opc2BRegOperandA), DATA_PHYADDR(0, ADDR_MODE_SP_REL));			
			break;
		case CPUOpcodes.OPC2B_PSH_Q: 
			regs[REG_SP] -= 8;
			WriteQword(regs[opc2BRegOperandA], DATA_PHYADDR(0, ADDR_MODE_SP_REL));	
			break;
		case CPUOpcodes.OPC2B_PSHI_B: 
			regs[REG_SP] -= 1;
			WriteByte((byte)(nInstr & 0xFF), DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			break; 
		case CPUOpcodes.OPC2B_POP_B: 
			WriteByteReg(ReadByte(DATA_PHYADDR(0, ADDR_MODE_SP_REL)), opc2BRegOperandA);
			regs[REG_SP] += 1;
			break;
		case CPUOpcodes.OPC2B_POP_D: 
			WriteDwordReg(ReadDword(DATA_PHYADDR(0, ADDR_MODE_SP_REL)), opc2BRegOperandA);
			regs[REG_SP] += 4;			
			break;
		case CPUOpcodes.OPC2B_POP_Q: 
			regs[opc2BRegOperandA] = ReadQword(DATA_PHYADDR(0, ADDR_MODE_SP_REL));
			regs[REG_SP] += 8;
			break;
		case CPUOpcodes.OPC2B_XSR: 
			pc = regs[REG_LK];
			break;
		case CPUOpcodes.OPC2B_RESET:
			//System.out.printf("*** RESET ***\n");
			Reset();
			break;
		case CPUOpcodes.OPC2B_BRK: 
			System.out.printf("*** Breakpoint (%016x) -- Press Any Key to Continue\n", pc);
			//System.in.read();
			break;
		case CPUOpcodes.OPC2B_XIT: 
			System.out.printf("*** Execution terminated by software.\n");
			PrintRegisterDump();
			return -1; 
		}
		
		return 1;
	}
	
	//translates logical address to physical address for Data/Stack access
	public long DATA_PHYADDR(int nOffset, int nAddrMode)
	{
		long logicalAddr = 0;
		long segAddr = 0;
		
		//first compute the logical address.
		switch(nAddrMode)
		{
		case ADDR_MODE_PTR0_REL: 
			logicalAddr = regs[REG_PTR0];
			break;
		case ADDR_MODE_PTR1_REL: 
			logicalAddr = regs[REG_PTR1];
			break;
		case ADDR_MODE_PTR2_REL: 
			logicalAddr = regs[REG_PTR2];
			break;
		case ADDR_MODE_PTR3_REL:
			logicalAddr = regs[REG_PTR3];
			break;
		case ADDR_MODE_SP_REL:
			logicalAddr = regs[REG_SP];
			break;
		case ADDR_MODE_FP_REL: 
			logicalAddr = regs[REG_FP];
			break;
		case ADDR_MODE_PC_REL: 
			logicalAddr = pc;
			break;
		case ADDR_MODE_ABS: 
		default:
			logicalAddr = 0;
		}
		logicalAddr += nOffset;
		
		//the segment address is selected from one of the segment registers.
		//the segment register selector is derived from the highest 5 bits (31:27) of the logical address
		segAddr = segregs[(int)((logicalAddr & 0xF8000000L) >> 27)];
		
		return (segAddr + (logicalAddr & 0x07FFFFFFL));
	}
	
	public void PrintRegisterDump()
	{
		System.out.printf("XQA = 0x%016X, XQB = 0x%016X, XQC = 0x%016X, XQD = 0x%016X,\n"
					+ "XQE = 0x%016X, XQF = 0x%016X, XQG = 0x%016X, XQH = 0x%016X,\n"
					+ "PTR0 = 0x%016X, PTR1 = 0x%016X, PTR2 = 0x%016X, PTR3 = 0x%016X,\n"
					+ "SP = 0x%016X, FP = 0x%016X, LK = 0x%016X, TEST = 0x%016X, PC = 0x%016X\n",
					regs[REG_XQA],
					regs[REG_XQB],
					regs[REG_XQC],
					regs[REG_XQD],
					regs[REG_XQE],
					regs[REG_XQF],
					regs[REG_XQG],
					regs[REG_XQH],
					regs[REG_PTR0],
					regs[REG_PTR1],
					regs[REG_PTR2],
					regs[REG_PTR3],
					regs[REG_SP],
					regs[REG_FP],
					regs[REG_LK],
					regs[REG_TEST],
					pc);
	}
	public int ReadDwordReg(int nReg)
	{
		switch(nReg)
		{
		case 0:
		default:
			return (int)(regs[REG_XQA] & 0xFFFFFFFFL);
		case 1:
			return (int)(regs[REG_XQA] >> 32);
		case 2:
			return (int)(regs[REG_XQB] & 0xFFFFFFFFL);
		case 3:
			return (int)(regs[REG_XQB] >> 32);
		case 4:
			return (int)(regs[REG_XQC] & 0xFFFFFFFFL);
		case 5:
			return (int)(regs[REG_XQC] >> 32);
		case 6:
			return (int)(regs[REG_XQD] & 0xFFFFFFFFL);
		case 7:
			return (int)(regs[REG_XQD] >> 32);
		case 8:
			return (int)(regs[REG_XQE] & 0xFFFFFFFFL);
		case 9:
			return (int)(regs[REG_XQE] >> 32);
		case 10:
			return (int)(regs[REG_XQF] & 0xFFFFFFFFL);
		case 11:
			return (int)(regs[REG_XQF] >> 32);
		case 12:
			return (int)(regs[REG_XQG] & 0xFFFFFFFFL);
		case 13:
			return (int)(regs[REG_XQG] >> 32);
		case 14:
			return (int)(regs[REG_XQH] & 0xFFFFFFFFL);
		case 15:
			return (int)(regs[REG_XQH] >> 32);
		}
	}
	public byte ReadByteReg(int nReg)
	{
		switch(nReg)
		{
		case 0:
		default:
			return (byte)(regs[REG_XQA] & 0xFFL);
		case 1:
			return (byte)((regs[REG_XQA] >> 8) & 0xFFL);
		case 2:
			return (byte)((regs[REG_XQA] >> 16) & 0xFFL);
		case 3:
			return (byte)((regs[REG_XQA] >> 24) & 0xFFL);
		case 4:
			return (byte)((regs[REG_XQA] >> 32) & 0xFFL);
		case 5:
			return (byte)((regs[REG_XQA] >> 40) & 0xFFL);
		case 6:
			return (byte)((regs[REG_XQA] >> 48) & 0xFFL);
		case 7:
			return (byte)((regs[REG_XQA] >> 56) & 0xFFL);
		case 8:
			return (byte)(regs[REG_XQB] & 0xFFL);
		case 9:
			return (byte)((regs[REG_XQB] >> 8) & 0xFFL);
		case 10:
			return (byte)((regs[REG_XQB] >> 16) & 0xFFL);
		case 11:
			return (byte)((regs[REG_XQB] >> 24) & 0xFFL);
		case 12:
			return (byte)((regs[REG_XQB] >> 32) & 0xFFL);
		case 13:
			return (byte)((regs[REG_XQB] >> 40) & 0xFFL);
		case 14:
			return (byte)((regs[REG_XQB] >> 48) & 0xFFL);
		case 15:
			return (byte)((regs[REG_XQB] >> 56) & 0xFFL);
		}
	}
	public void WriteDwordReg(int nValue, int nReg)
	{
		long x = (nValue & 0xFFFFFFFF);
		
		switch(nReg)
		{
		case 0:
		default:
			regs[REG_XQA] &= 0xFFFFFFFF00000000L;
			regs[REG_XQA] |= x;
			break;
		case 1:
			regs[REG_XQA] &= 0xFFFFFFFFL;
			regs[REG_XQA] |= (x << 32);
			break;
		case 2:
			regs[REG_XQB] &= 0xFFFFFFFF00000000L;
			regs[REG_XQB] |= x;
			break;
		case 3:
			regs[REG_XQB] &= 0xFFFFFFFFL;
			regs[REG_XQB] |= (x << 32);
			break;
		case 4:
			regs[REG_XQC] &= 0xFFFFFFFF00000000L;
			regs[REG_XQC] |= x;
			break;
		case 5:
			regs[REG_XQC] &= 0xFFFFFFFFL;
			regs[REG_XQC] |= (x << 32);
			break;
		case 6:
			regs[REG_XQD] &= 0xFFFFFFFF00000000L;
			regs[REG_XQD] |= x;
			break;
		case 7:
			regs[REG_XQD] &= 0xFFFFFFFFL;
			regs[REG_XQD] |= (x << 32);
			break;
		case 8:
			regs[REG_XQE] &= 0xFFFFFFFF00000000L;
			regs[REG_XQE] |= x;
			break;
		case 9:
			regs[REG_XQE] &= 0xFFFFFFFFL;
			regs[REG_XQE] |= (x << 32);
			break;
		case 10:
			regs[REG_XQF] &= 0xFFFFFFFF00000000L;
			regs[REG_XQF] |= x;
			break;
		case 11:
			regs[REG_XQF] &= 0xFFFFFFFFL;
			regs[REG_XQF] |= (x << 32);
			break;
		case 12:
			regs[REG_XQG] &= 0xFFFFFFFF00000000L;
			regs[REG_XQG] |= x;
			break;
		case 13:
			regs[REG_XQG] &= 0xFFFFFFFFL;
			regs[REG_XQG] |= (x << 32);
			break;
		case 14:
			regs[REG_XQH] &= 0xFFFFFFFF00000000L;
			regs[REG_XQH] |= x;
			break;
		case 15:
			regs[REG_XQH] &= 0xFFFFFFFFL;
			regs[REG_XQH] |= (x << 32);
			break;
		}
	}
	public void WriteByteReg(byte nValue, int nReg)
	{
		long x = (nValue & 0xFF);
		
		switch(nReg)
		{
		case 0:
		default:
			regs[REG_XQA] &= 0xFFFFFFFFFFFFFF00L;
			regs[REG_XQA] |= x;
			break;
		case 1:
			regs[REG_XQA] &= 0xFFFFFFFFFFFF00FFL;
			regs[REG_XQA] |= (x << 8);
			break;
		case 2:
			regs[REG_XQA] &= 0xFFFFFFFFFF00FFFFL;
			regs[REG_XQA] |= (x << 16);
			break;
		case 3:
			regs[REG_XQA] &= 0xFFFFFFFF00FFFFFFL;
			regs[REG_XQA] |= (x << 24);
			break;
		case 4:
			regs[REG_XQA] &= 0xFFFFFF00FFFFFFFFL;
			regs[REG_XQA] |= (x << 32);
			break;
		case 5:
			regs[REG_XQA] &= 0xFFFF00FFFFFFFFFFL;
			regs[REG_XQA] |= (x << 40);
			break;
		case 6:
			regs[REG_XQA] &= 0xFF00FFFFFFFFFFFFL;
			regs[REG_XQA] |= (x << 48);
			break;
		case 7:
			regs[REG_XQA] &= 0x00FFFFFFFFFFFFFFL;
			regs[REG_XQA] |= (x << 56);
			break;
		case 8:
			regs[REG_XQB] &= 0xFFFFFFFFFFFFFF00L;
			regs[REG_XQB] |= x;
			break;
		case 9:
			regs[REG_XQB] &= 0xFFFFFFFFFFFF00FFL;
			regs[REG_XQB] |= (x << 8);
			break;
		case 10:
			regs[REG_XQB] &= 0xFFFFFFFFFF00FFFFL;
			regs[REG_XQB] |= (x << 16);
			break;
		case 11:
			regs[REG_XQB] &= 0xFFFFFFFF00FFFFFFL;
			regs[REG_XQB] |= (x << 24);
			break;
		case 12:
			regs[REG_XQB] &= 0xFFFFFF00FFFFFFFFL;
			regs[REG_XQB] |= (x << 32);
			break;
		case 13:
			regs[REG_XQB] &= 0xFFFF00FFFFFFFFFFL;
			regs[REG_XQB] |= (x << 40);
			break;
		case 14:
			regs[REG_XQB] &= 0xFF00FFFFFFFFFFFFL;
			regs[REG_XQB] |= (x << 48);
			break;
		case 15:
			regs[REG_XQB] &= 0x00FFFFFFFFFFFFFFL;
			regs[REG_XQB] |= (x << 56);
			break;
		}
	}
	
	public long ReadQword(long nAddress)
	{
		int addr = (int)nAddress;
		
		long t0 = memory[addr];
		long t1 = memory[addr + 1];
		long t2 = memory[addr + 2];
		long t3 = memory[addr + 3];
		long t4 = memory[addr + 4];
		long t5 = memory[addr + 5];
		long t6 = memory[addr + 6];
		long t7 = memory[addr + 7];
		
		//java is big endian
		return ((t0 << 56) | (t1 << 48) | (t2 << 40) | (t3 << 32) | (t4 << 24) | (t5 << 16) | (t6 << 8) | t7);
	}
	public int ReadDword(long nAddress)
	{
		int addr = (int)nAddress;
		
		int t0 = memory[addr] & 0xFF;
		int t1 = memory[addr + 1] & 0xFF;
		int t2 = memory[addr + 2] & 0xFF;
		int t3 = memory[addr + 3] & 0xFF;
		
		//System.out.printf("fetched dword %08x, %08x, %08x, %08x\n", t0, t1, t2, t3);
		
		//java is big endian
		return ((t0 << 24) | (t1 << 16) | (t2 << 8) | t3);
	}
	public byte ReadByte(long nAddress)
	{
		return memory[(int)nAddress];
	}
	
	public void WriteByte(byte nValue, long nAddress)
	{
		memory[(int)nAddress] = nValue;
	}
	public void WriteDword(int nValue, long nAddress)
	{
		memory[(int)nAddress] = (byte)(nValue >> 24);
		memory[((int)nAddress) + 1] = (byte)((nValue >> 16) & 0xFF);
		memory[((int)nAddress) + 2] = (byte)((nValue >> 8) & 0xFF);
		memory[((int)nAddress) + 3] = (byte)(nValue & 0xFF);
	}
	public void WriteQword(long nValue, long nAddress)
	{
		memory[(int)nAddress] = (byte)(nValue >> 56);
		memory[((int)nAddress) + 1] = (byte)((nValue >> 48) & 0xFFL);
		memory[((int)nAddress) + 2] = (byte)((nValue >> 40) & 0xFFL);
		memory[((int)nAddress) + 3] = (byte)((nValue >> 32) & 0xFFL);
		memory[((int)nAddress) + 4] = (byte)((nValue >> 24) & 0xFFL);
		memory[((int)nAddress) + 5] = (byte)((nValue >> 16) & 0xFFL);
		memory[((int)nAddress) + 6] = (byte)((nValue >> 8) & 0xFFL);
		memory[((int)nAddress) + 7] = (byte)(nValue & 0xFFL);
	}
	
	//virtual RAM
	private byte[] memory;
	
	//addressable registers
	private long[] regs;
	
	//segment registers
	private long[] segregs;
	
	//non-addressable registers
	private long pc;
}
