package emris.server;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader readerFromClient; ///stream for read  data from cliont
    private final BufferedWriter writerToClient; /// stream for write data  to client

    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        readerFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writerToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

    }

    private void close() throws IOException {

            readerFromClient.close();
            writerToClient.close();
            clientSocket.close();
        }
    private boolean checkDirectory(File dir){
        if(!dir.exists()){
            return dir.mkdir();
        }
       return true;
    }

        @Override
        public void run () {
            try {
                File dir = new File("./upload/");
                if(!checkDirectory(dir)){
                    writerToClient.write("can't create directory. Server error");
                    writerToClient.flush();
                    close();
                    return;

                }

                String fileName = readerFromClient.readLine();
                long fileSize = Integer.parseInt(readerFromClient.readLine());

                System.out.println("got file_name: " + fileName + "\n" + "got file_Size: " + fileSize);
                writerToClient.write("Hello! confirm file_mame: " + fileName + "\n");
                writerToClient.write("confirm file_size: " + fileSize + " bytes\n");
                writerToClient.flush();
                File file = new File(dir.getAbsoluteFile() + "/" + fileName);
                FileOutputStream writerToFile = new FileOutputStream(file);
                long readySize = 0;
                byte[] buffer = new byte[1024];
                InputStream inputStream = clientSocket.getInputStream();
                while (readySize < fileSize){
                    int read = inputStream.read(buffer);
                    System.out.println(read);
                    readySize += read;
                    writerToFile.write(buffer,0,read);
                }
                System.out.println("done!!!");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
