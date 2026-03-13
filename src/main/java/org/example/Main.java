package org.example;


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Main
{
    public static HashMap<String, Map.Entry<Long, Long>> GetAllFiles(HashMap<String, Map.Entry<Long, Long>> extensions, Path directory)
    {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory))
        {
            for(Path path : stream)
            {
                if(path.toFile().isDirectory())
                {
                    GetAllFiles(extensions, path);
                }
                else
                {
                    String ext = GetFileExtension(path);
                    if(!extensions.containsKey(ext))
                    {
                        extensions.put(ext, Map.entry(0L, 0L));
                    }
                    extensions.computeIfPresent(ext, (k, entry) ->
                            Map.entry(entry.getKey() + 1, entry.getValue() + path.toFile().length()));
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
        }
        return extensions;
    }

    public static String GetFileExtension(Path path)
    {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');

        if(dot == -1 || dot == fileName.length() - 1)
        {
            return "";
        }
        else
        {
            return fileName.substring(dot + 1);
        }
    }


    static void main()
    {
        HashMap<String, Map.Entry<Long, Long>> extensions = new HashMap<>();
        Path directory = Paths.get("D:\\");
        List<String> lines = new ArrayList<>();

        GetAllFiles(extensions, directory);
        extensions = extensions.entrySet().stream()
                .sorted(Comparator.comparing(entry -> (-1) * entry.getValue().getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        AtomicLong totalNumber = new AtomicLong(0L);
        AtomicLong totalSize = new AtomicLong(0L);
        extensions.forEach((key, value) ->
        {
            totalNumber.addAndGet(value.getKey());
            totalSize.addAndGet(value.getValue());
        });

        lines.add(String.format("%-50s|%-10s|%-20s|%-20s|%-20s|", "Extension", "Number", "Size", "% of Number", "% of Size"));
        for(int i = 0; i < (Math.min(extensions.size(), 50)); i++)
        {
            var key = extensions.keySet().toArray()[i];
            var value = extensions.get(key);

            lines.add(String.format("%-50s|%-10s|%-20s|%-20.2f|%-20.5f|",
                    key, value.getKey(), value.getValue(),
                    ((value.getKey() * 1.0) / totalNumber.get()), ((value.getValue() * 1.0) / totalSize.get())
            ));
        }
        lines.add(String.format("%-50s|%-10s|%-20s|", "Total", totalNumber.get(), totalSize.get()));
        try
        {
            Files.write(Paths.get("extensions.txt"), lines, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

