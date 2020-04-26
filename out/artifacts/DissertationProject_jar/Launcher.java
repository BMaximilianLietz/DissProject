import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {
	public static void main(String[] args) {
			System.out.println("Test that this is the right version - 19/04/2020");
			//System.out.println("Working Directory = " + System.getProperty("user.dir"));
			System.out.println("java --module-path \"" + System.getProperty("user.dir") + "\\javafx-sdk-11.0.2\\lib\" --add-modules javafx.controls,javafx.fxml -jar DissertationProject.jar");
			
			File fileRelative = new File("launcher.bat");
			File log= new File(fileRelative.getAbsolutePath());
			String launchCommand = "java --module-path \"" + System.getProperty("user.dir") 
				+ "\\javafx-sdk-11.0.2\\lib\" --add-modules javafx.controls,javafx.fxml -jar DissertationProject.jar";
				
			try {
                FileWriter fw = new FileWriter(log);
				fw.write(launchCommand);
				fw.close();
            } catch (Exception e) {
				System.out.println(e);
			}				
			try{    
				Process p = Runtime.getRuntime().exec("cmd /c start \"\" launcher.bat");
				p.waitFor();

			}catch( Exception ex ){
				//Validate the case the file can't be accesed (not enought permissions)
				System.out.println(ex);

			}
			System.out.println("Loading successful - now starting program...");
			//exec("cmd /c start \"\" launcher.bat");
	}
}