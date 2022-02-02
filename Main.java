package vnocs_emu;

public class Main 
{
	public static void main(String[] args) throws Exception
	{
		CPUVirtualMachine vm = new CPUVirtualMachine();
		
		vm.LoadImageFromFile(args[0]);
		
		while(vm.SingleStep() >= 0) {}
	}
}
