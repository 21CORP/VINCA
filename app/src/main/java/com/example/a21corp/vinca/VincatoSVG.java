package com.example.a21corp.vinca;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Environment;
import android.util.Log;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Sebastian on 1/15/2017.
 */

public class VincatoSVG {
    public static class IconWidth{
        public static final float conclusion = 120;
        public static final float activity = 290;
        public static final float pause = 275;
        public static final float iterationEnd = 135;
        public static final float iterationBegin = 135;
        public static final float projectBegin = 135;
        public static final float projectEnd = 115;
        public static final float processBegin = 122;
        public static final float processEnd = 120;
    }
    public static class Rect{
        public int x;
        public int y;
    }
    private static String format = "<use x=\"%d\" y=\"%d\" xlink:href=\"#%s\"/>\n";

    public static File getSVGFile(Activity act, List<Container> elements, String name) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "VINCA");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + name + ".svg";
        File dst = new File(filename);
        try {
            FileWriter stream = new FileWriter(dst);
            Resources resources = act.getResources();
            BufferedReader in = new BufferedReader( new InputStreamReader(resources.openRawResource(R.raw.vincadefs)));
            String line; //Copy the template into the file
            while((line = in.readLine()) != null)
            {
                stream.write(line + '\n');
            }
            Rect r = new Rect();
            for (VincaElement v : elements) {
                writeElement(stream, v, r);
            }
            stream.write("</svg>");
            stream.flush();
            in.close();
            stream.close();
            Log.i("SVG export", "Created new file at " + dst.getPath());
            return dst;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SVG export", "Failed to create stream");
            return null;
        }
    }
    private static void writeElement(FileWriter stream, VincaElement element, Rect r) throws IOException {
        switch (element.type){
            case VincaElement.ELEMENT_ACTIVITY: //TODO: Add methods....
            {
                stream.write(String.format(format, r.x, r.y, "activity"));
                r.x += IconWidth.activity;
                break;
            }
            case VincaElement.ELEMENT_DECISION:
            {
                stream.write(String.format(format, r.x, r.y, "conclusion"));
                r.x += IconWidth.conclusion;
                break;
            }
            case VincaElement.ELEMENT_ITERATE:
                stream.write(String.format(format, r.x, r.y, "iterationStart"));
                r.x += IconWidth.iterationBegin;
                writeContainer(stream, element, r);
                stream.write(String.format(format, r.x, r.y, "iterationEnd"));
                r.x += IconWidth.iterationEnd;
                break;
            case VincaElement.ELEMENT_METHOD:
                stream.write("<!--- fuck this shit -->");
                break;
            case VincaElement.ELEMENT_PAUSE:
                stream.write(String.format(format, r.x, r.y, "pause"));
                r.x += IconWidth.pause;
                break;
            case VincaElement.ELEMENT_PROCESS:
                stream.write(String.format(format, r.x, r.y, "processBegin"));
                r.x += IconWidth.processBegin;
                writeContainer(stream, element, r);
                stream.write(String.format(format, r.x, r.y, "processEnd"));
                r.x += IconWidth.processEnd;
                break;
            case VincaElement.ELEMENT_PROJECT:
                stream.write(String.format(format, r.x, r.y, "projectBegin"));
                r.x += IconWidth.projectBegin;
                writeContainer(stream, element, r);
                stream.write(String.format(format, r.x, r.y, "projectEnd"));
                r.x += IconWidth.projectEnd;
                break;
        }
    }
    private static void writeContainer(FileWriter stream, VincaElement element, Rect r) throws IOException
    {
        Container c = (Container)element;
        for (VincaElement e: c.containerList) {
            writeElement(stream, e, r);
        }
    }
}
