import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FlashXELL {
	static String SOFTWARE="E:\\Consolas\\Xbox360\\Programas\\JTAGGlitch\\Software\\";
	static String XILINK="C:\\Xilinx\\13.2\\LabTools\\settings32.bat C:\\Xilinx\\13.2\\LabTools\\LabTools\\bin\\nt\\impact.exe";
	static String NANDPRO=SOFTWARE+"Nandpro20e\\";
	static String PYTHON="C:\\Python27\\";
	static String GGBUILD=SOFTWARE+"EasyggBuild0334\\Run.exe";
	static String DIRCOPIA="E:\\Consolas\\Xbox360\\Programas\\JTAGGlitch\\NANDS\\";
	static String PLACA="";
	static String NSERIE="";
	static String TIPO="";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try {
        	int opcion=0;
			PLACA=args[0];
			NSERIE=args[1];
			TIPO=args[2];
			DIRCOPIA=DIRCOPIA+NSERIE+"\\";
			eliminarFicheroUsuario();
			File f=new File(DIRCOPIA);
			if(!f.exists()){
				if(f.mkdir()){
					File f1=new File(DIRCOPIA+"cpukey.txt");
					File f2=new File(DIRCOPIA+"dvdkey.txt");
					f1.createNewFile();
					f2.createNewFile();
				}
			}
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(DIRCOPIA+"numeroSerie.txt"));
			bos.write(NSERIE.getBytes());
			bos.flush();
			bos.close();
			System.out.println("LA PLACA ES: "+TIPO+" "+PLACA);
			System.out.println("LA PLACA ES: "+PLACA);
			while(opcion!=-1){
				System.out.println("MENU DE OPCIONES");
				System.out.println("1-Programar Glitch");
				System.out.println("2-Leer Nands");
				System.out.println("3-Crear ECC");
				System.out.println("4-Programar ECC");
				System.out.println("5-Exit");
				System.out.println("Introduce una opcion");				
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader (isr);
				opcion=Integer.parseInt(br.readLine());
				switch(opcion){
					case 1:
						programarGlitch();
						break;
					case 2:
						leerNands();
						break;
					case 3:
						crearECC();
						break;
					case 4:
						programarECC();
						break;
					case 5:
						opcion=-1;
						break;
				}
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
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
	private static boolean programarGlitch(){
		boolean dev=false;
		try{
			
			Runtime rt=Runtime.getRuntime();
			Process proc=null;
			proc=rt.exec(XILINK);
			System.out.println("**********************");
			System.out.println("**** GLITCH PROGRAMADO ****");
			System.out.println("**********************");
			System.out.println("AHORA ENCHUFA CONECTA EL GLITCH A LA PLACA");
			if ("FAT".equals(TIPO)){
				rt.exec("cmd.exe /C explorer.exe "+SOFTWARE+"FAT\\wiring_front.jpg");
				rt.exec("cmd.exe /C explorer.exe "+SOFTWARE+"FAT\\wiring_back.jpg");
			}else{
				rt.exec("cmd.exe /C explorer.exe "+SOFTWARE+"SLIM\\slim_wiring_front.jpg");
				rt.exec("cmd.exe /C explorer.exe "+SOFTWARE+"SLIM\\slim_wiring_back.jpg");
				
			}
			dev=true;
		}catch(Exception e){
			e.printStackTrace();
			dev=false;
		}
		return dev;
	}
	private static boolean leerNands(){
		boolean dev=false;
		try {
			Runtime rt=Runtime.getRuntime();
			Process proc=null;
			if("16".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"16nandread.bat");
			}else if("256".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"256nandread.bat");
			}else if("512".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"512nandread.bat");
			}
			StreamGlobber sg=new StreamGlobber(proc.getInputStream());
			sg.run();
			InputStream retStream = sg.getRetStream();
			System.out.println(retStream.read());
			while(retStream.read()!=-1){
				System.out.println(retStream.read());
			}
			boolean copiad=false;
			System.out.println("COPIAMOS EL NAND1 AL DIRUSUARIO");
			copiad=copiarFichero(NANDPRO+"nand.bin",DIRCOPIA+"nand.bin");
			System.out.println("COPIADO EL NAND1 AL DIRUSUARIO "+copiad);
			System.out.println("COPIAMOS EL NAND2 AL DIRUSUARIO");
			copiad=copiarFichero(NANDPRO+"nand2.bin",DIRCOPIA+"nand2.bin");
			System.out.println("COPIADO EL NAND2 AL DIRUSUARIO "+copiad);
			System.out.println("COPIAMOS EL NAND1 AL DIRECTORIO DE PYTHON");
			copiad=copiarFichero(NANDPRO+"nand.bin",PYTHON+"nand.bin");
			System.out.println("COPIADO EL NAND1 AL DIRECTORIO DE PYTHON "+copiad);
			dev=true;
			
		} catch (Exception e) {
			e.printStackTrace();
			dev=false;
		}
		return dev;
		
	}
	private static boolean crearECC(){
		boolean dev=false;
		try {
			boolean copiad=false;
			Runtime rt=Runtime.getRuntime();
			Process proc=null;
			if("JASPER".equals(TIPO)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"JASPERpython.bat");
			}else{
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"python.bat");
			}
			StreamGlobber sg=new StreamGlobber(proc.getInputStream());
			sg.run();
			InputStream retStream = sg.getRetStream();
			System.out.println(retStream.read());
			while(retStream.read()!=-1){
				System.out.println(retStream.read());
			}
			
			System.out.println("COPIAMOS EL ECC AL DIRUSUARIO");
			copiad=copiarFichero(PYTHON+"output\\image_00000000.ecc",DIRCOPIA+"image_00000000.ecc");
			System.out.println("COPIADO EL ECC AL DIRUSUARIO "+copiad);
			
			System.out.println("COPIAMOS EL ECC AL NANDPRO");
			copiad=copiarFichero(PYTHON+"output\\image_00000000.ecc",NANDPRO+"image_00000000.ecc");
			System.out.println("COPIADO EL ECC AL NANDPRO "+copiad);
			
			System.out.println("COPIAMOS EL CB_A.bin AL NANDPRO");
			copiad=copiarFichero(PYTHON+"output\\CB_A.bin",DIRCOPIA+"CB_A.bin");
			System.out.println("COPIADO EL CB_A.bin AL NANDPRO "+copiad);
			
			System.out.println("COPIAMOS EL CB_B.bin AL NANDPRO");
			copiad=copiarFichero(PYTHON+"output\\CB_B.bin",DIRCOPIA+"CB_B.bin");
			System.out.println("COPIADO EL CB_B.bin AL NANDPRO "+copiad);
			
			System.out.println("COPIAMOS EL SMC.bin AL NANDPRO");
			copiad=copiarFichero(PYTHON+"output\\SMC.bin",DIRCOPIA+"SMC.bin");
			System.out.println("COPIADO EL SMC.bin AL NANDPRO "+copiad);
			
			dev=true;
		} catch (Exception e) {
			e.printStackTrace();
			dev=false;
		}
		return dev;
		
	}
	private static boolean programarECC(){
		boolean dev=false;
		try {
			Runtime rt=Runtime.getRuntime();
			Process proc=null;
			if("16".equals(PLACA)){
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"16nandwriteECC.bat");
			}else{
				proc=rt.exec("cmd.exe /C start "+SOFTWARE+"256512nandwriteECC.bat");
			}
			StreamGlobber sg=new StreamGlobber(proc.getInputStream());
			sg.run();
			InputStream retStream = sg.getRetStream();
			System.out.println(retStream.read());
			while(retStream.read()!=-1){
				System.out.println(retStream.read());
			}
			System.out.println("ENCIENDE LA CONSOLA");
			System.out.println("ESCRIBE EL CPUKEY Y EL DVDKEY EN LOS FICHEROS NECESARIOS");
			rt.exec("cmd.exe /C notepad "+DIRCOPIA+"cpukey.txt");
			rt.exec("cmd.exe /C notepad "+DIRCOPIA+"dvdkey.txt");
			dev=true;
		} catch (Exception e) {
			e.printStackTrace();
			dev=false;
		}
		return dev;
		
	}
	static boolean eliminarFicheroUsuario(){
		boolean dev=false;
		try {
			String opcion="N";
			File ff=new File(DIRCOPIA);
			if(ff.exists()){
				System.out.println("Se han encontrado ficheros");
				System.out.println("Desea eliminarlos?");
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader (isr);
				opcion=String.valueOf(br.readLine());
				while(!"S".equals(opcion.toUpperCase())&& !"N".equals(opcion.toUpperCase())){
					opcion=String.valueOf(br.readLine().toUpperCase());
				}
			}
			int x=1;
			if("S".equals(opcion.toUpperCase())){
				File ff2=new File(DIRCOPIA+"BACKUP");
				while(ff2.exists()){
					ff2=new File(DIRCOPIA+"BACKUP"+x);
					x=x+1;
				}
				ff2.mkdirs();
				File[] listFiles = ff.listFiles();
				for (int i = 0; i < listFiles.length; i++) {
					File file = listFiles[i];
					if(!file.isDirectory()){
						file.renameTo(new File(ff2+"\\"+file.getName()));
						boolean delete = file.exists();
						System.out.println(file.getName()+" movido backup: "+delete);
					}
				}

			}
			dev=true;
		} catch (Exception e) {
			e.printStackTrace();
			dev=false;
		}
		return dev;
		
	}
}
