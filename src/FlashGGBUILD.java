import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FlashGGBUILD {
	static String PLACA="";
	static String NSERIE="";
	static String TIPO="";
	static String SOFTWARE="E:\\Consolas\\Xbox360\\Programas\\JTAGGlitch\\Software\\";
	static String XILINK="C:\\Xilinx\\13.2\\LabTools\\settings32.bat C:\\Xilinx\\13.2\\LabTools\\LabTools\\bin\\nt\\impact.exe";
	static String NANDPRO=SOFTWARE+"Nandpro20e\\";
	static String PYTHON="C:\\Python27\\";
	static String GGBUILD=SOFTWARE+"EasyggBuild0334\\";
	static String DIRCOPIA="E:\\Consolas\\Xbox360\\Programas\\JTAGGlitch\\NANDS\\";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		PLACA=args[0];
		NSERIE=args[1];
		TIPO=args[2];
		System.out.println("LA PLACA ES: "+TIPO+" "+PLACA);
		System.out.println("LA PLACA ES: "+PLACA);
		DIRCOPIA=DIRCOPIA+NSERIE+"\\";
		copiarFichero(DIRCOPIA+"nand.bin",GGBUILD+"Data\\my360\\nanddump.bin");
		copiarFichero(DIRCOPIA+"cpukey.txt",GGBUILD+"Data\\my360\\cpukey.txt");		
		Runtime rt=Runtime.getRuntime();
		Process proc=null;
		proc=rt.exec("cmd.exe /C start "+SOFTWARE+"GGBUILD.bat");
		StreamGlobber sg=new StreamGlobber(proc.getInputStream());
		sg.run();
		InputStream retStream = sg.getRetStream();
		System.out.println(retStream.read());
		while(retStream.read()!=-1){
			System.out.println(retStream.read());
		}
		copiarFichero(GGBUILD+"Data\\updflash.bin",DIRCOPIA+"updflash.bin");
		copiarFichero(GGBUILD+"Data\\updflash.bin",NANDPRO+"updflash.bin");
		escribirNands();
		
	}
	private static boolean copiarFichero(String ficheroO,String ficheroD){
		boolean copiado =false;
		try {
			File fo=new File(ficheroO);
			File fd=new File(ficheroD);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fo));
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fd));
	        int read = bis.read();
	        while(read!=-1){
	        	bos.write(read);
	        	read=bis.read();
	        }
	        bos.flush();
	        bis.close();
	        bos.close();
			copiado=true;
		} catch (Exception e) {
			e.printStackTrace();
			copiado=false;
		}
		return copiado;
	}
	private static boolean escribirNands(){
		boolean dev=false;
		try {
			Runtime rt=Runtime.getRuntime();
			Process proc=null;
			if("16".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"16nandwrite.bat");
			}else if("256".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"256nandwrite.bat");
			}else if("512".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"512nandwrite.bat");
			}
			StreamGlobber sg=new StreamGlobber(proc.getInputStream());
			sg.run();
			InputStream retStream = sg.getRetStream();
			System.out.println(retStream.read());
			while(retStream.read()!=-1){
				System.out.println(retStream.read());
			}
			boolean copiad=false;
			dev=true;
			
		} catch (Exception e) {
			e.printStackTrace();
			dev=false;
		}
		return dev;
		
	}
}
