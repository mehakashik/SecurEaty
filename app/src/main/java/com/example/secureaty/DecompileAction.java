package com.example.secureaty;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class DecompileAction extends AsyncTask<File, Integer, String> {
    private Exception exception;
    private String decompileUrl = "http://app.apkdecompilers.com/app.php";
    private Pattern decompiledZipPattern = Pattern.compile("https:\\/\\/app.apkdecompilers.com\\/download\\/.*\\.apk\\.zip");
    private DecompileItem item;

    protected String doInBackground(File... file) {
        try {
                publishProgress(5);
            String downloadUrl = decompile(file[0]);
                publishProgress(30);
            File zipFile = download(downloadUrl);
                publishProgress(50);
            File unpackedDir = unzip(zipFile);
                publishProgress(60);
            String code = extractSources(unpackedDir);
                publishProgress(70);
            Log.d("Done", "Symbols read: " + code.length());

            boolean DOAdetected = DOADetector.detect(code);
                publishProgress(80);
            boolean MetasploitDetected = MetasploitDetector.detect(code);
                publishProgress(90);
            boolean OverlayDetected = OverlayDetector.detect(code);
                publishProgress(100);

            if(DOAdetected) {
                this.item.setAttack(Attack.Doa);
            }
            else if (MetasploitDetected) {
                this.item.setAttack(Attack.Metasploit);
            }
            else if (OverlayDetected) {
                this.item.setAttack(Attack.Overlay);
            }
            Log.d("DOAdetected", String.valueOf(DOAdetected));
            Log.d("Metasploitdetected", String.valueOf(MetasploitDetected));

        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    public void setItem(DecompileItem item){
        this.item = item;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.d("onProgressUpdate", "yes");
        this.item.setProgress(progress[0]);
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }

    private String decompile(File file) throws IOException  {
        Log.d("Decompile file", file.getName());
        for(int i = 0; i<2; i++) {
            try {
                MultipartUtility multipart = new MultipartUtility(decompileUrl, "UTF-8");
                multipart.addFilePart("apk", file);
                String response = multipart.finish(); // response from server.
                Matcher m = decompiledZipPattern.matcher(response);
                m.find();
                String downloadUrl = m.group().replace("https", "http");
                Log.d("Decompile file", "Success: " + downloadUrl);
                return downloadUrl;
            } catch (IOException e){
                Log.d("Decompile file response", "Error: " + e.toString() + ". Retrying...");
            }

        }
        throw new IOException("Something went wrong");
    }

    private File download(String url) throws IOException {
        Log.d("File download", url);
        try {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(
                        "Server returned HTTP " + connection.getResponseCode() + " "
                                + connection.getResponseMessage()
                );
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
    //            output = new FileOutputStream("/sdcard/file_name.extension");
            int index = url.lastIndexOf('/');

            File zipFile = File.createTempFile(url.substring(index+1), ".zip", null);
            FileOutputStream outputStream = new FileOutputStream(zipFile);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                outputStream.write(data, 0, count);
            }
            Log.d("File download", "Success: " + zipFile);
            return zipFile;
        } catch (IOException e) {
            Log.d("File download", "Error: " + e.getMessage());
            throw e;
        }
    }

    private File unzip(File zip) throws ZipException {
        String zipPath = zip.getPath();
        Log.d("Unzip", zipPath);
        try {
            String unzipDestination = zipPath.substring(0, zipPath.length() - 4);
            File unzipDir = new File(unzipDestination);
            unzipDir.mkdir();
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(unzipDestination);
            Log.d("Unzip", "Success: " + unzipDestination);
            return unzipDir;
        } catch (ZipException e) {
            Log.d("Unzip", "Error: " + e.getMessage());
            throw e;
        }
    }

    private String extractSources(File unpackedApkDir) {
        StringBuilder result = new StringBuilder();
        File manifestFile = new File(unpackedApkDir.getPath() + "/AndroidManifest.xml");
        try {
            Scanner scanner = new Scanner(manifestFile);
            result.append(scanner.useDelimiter("\0").next());
            scanner.close();
        } catch (FileNotFoundException e) {
            //Log.d("Extracting sources", "File not found: " + manifestFile.getPath());
        }
        for (File file : getSourceFilesRecursive(unpackedApkDir)) {
            //Log.d("Extracting sources", "file: " + file);
            try {
                Scanner scanner = new Scanner(file);
                result.append(scanner.useDelimiter("\0").next());
                scanner.close();
            } catch (FileNotFoundException e) {
               // Log.d("Extracting sources", "File not found: " + file.getPath());
            }
        }
        Log.d("Extracting sources", "Done: " + unpackedApkDir.getPath());
        return result.toString();
    }

    private List<File> getSourceFilesRecursive(File pFile)
    {
        ArrayList<File> found = new ArrayList<>();
        for(File file : pFile.listFiles()) {
            if(file.isDirectory()) {
                found.addAll(getSourceFilesRecursive(file));
            } else if (file.getName().endsWith(".java")){
                found.add(file);
            }
        }
        return found;
    }
}