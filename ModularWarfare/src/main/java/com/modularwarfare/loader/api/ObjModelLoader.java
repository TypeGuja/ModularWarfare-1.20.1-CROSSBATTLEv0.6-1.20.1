package com.modularwarfare.loader.api;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.type.BaseType;
import org.joml.Vector3f;

import java.io.*;
import java.util.*;

public class ObjModelLoader {

    private static final Map<String, AbstractObjModel> MODEL_CACHE = new HashMap<>();
    private static final Set<String> DEBUG_PATHS = new HashSet<>();

    public static AbstractObjModel load(String path) {
        // Проверяем кэш
        if (MODEL_CACHE.containsKey(path)) {
            ModularWarfare.LOGGER.info("Loading OBJ from cache: " + path);
            return MODEL_CACHE.get(path);
        }

        ModularWarfare.LOGGER.info("=== Loading OBJ: " + path + " ===");

        String normalizedPath = normalizePath(path);
        InputStream is = null;
        String source = null;

        // 1. Пробуем из classpath/resources
        is = tryLoadFromClasspath(normalizedPath);
        if (is != null) source = "classpath";

        // 2. Пробуем из файловой системы
        if (is == null) {
            is = tryLoadFromFileSystem(normalizedPath);
            if (is != null) source = "filesystem";
        }

        if (is == null) {
            ModularWarfare.LOGGER.error("OBJ file not found: " + path);
            ModularWarfare.LOGGER.error("Tried the following paths:");
            for (String p : DEBUG_PATHS) {
                ModularWarfare.LOGGER.error("  - " + p);
            }
            DEBUG_PATHS.clear();
            return createEmptyModel();
        }

        ModularWarfare.LOGGER.info("Found OBJ at: " + source);

        try {
            AbstractObjModel model = parseObjFile(is, path);
            if (model.getParts().isEmpty()) {
                ModularWarfare.LOGGER.warn("OBJ file parsed but no parts found: " + path);
            } else {
                ModularWarfare.LOGGER.info("OBJ parsed successfully: " + model.getParts().size() + " parts");
                for (String partName : model.getParts().keySet()) {
                    ModularWarfare.LOGGER.info("  - Part: " + partName);
                }
            }

            MODEL_CACHE.put(path, model);
            return model;
        } catch (Exception e) {
            ModularWarfare.LOGGER.error("Failed to parse OBJ: " + path, e);
            return createEmptyModel();
        } finally {
            try { if (is != null) is.close(); } catch (Exception e) {}
        }
    }

    public static AbstractObjModel load(BaseType type, String path) {
        return load(path);
    }

    private static String normalizePath(String path) {
        if (path == null) return "";

        // Убираем начальные слэши
        while (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1);
        }
        // Убираем assets/ если есть
        if (path.startsWith("assets/")) {
            path = path.substring(7);
        }
        // Убираем modularwarfare/ если есть
        if (path.startsWith("modularwarfare/")) {
            path = path.substring(15);
        }
        return path;
    }

    private static InputStream tryLoadFromClasspath(String path) {
        ClassLoader classLoader = ObjModelLoader.class.getClassLoader();

        String[] pathsToTry = {
                path,
                "assets/modularwarfare/" + path,
                "assets/modularwarfare/obj/" + path,
                "assets/modularwarfare/obj/guns/" + path,
                "obj/" + path,
                "obj/guns/" + path
        };

        for (String tryPath : pathsToTry) {
            DEBUG_PATHS.add("classpath:" + tryPath);
            try {
                InputStream is = classLoader.getResourceAsStream(tryPath);
                if (is != null) {
                    ModularWarfare.LOGGER.info("  Found in classpath: " + tryPath);
                    return is;
                }
            } catch (Exception e) {
                // Продолжаем
            }
        }

        return null;
    }

    private static InputStream tryLoadFromFileSystem(String path) {
        try {
            File modDir = ModularWarfare.MOD_DIR;
            if (modDir != null && modDir.exists()) {
                String[] pathsToTry = {
                        path,
                        "obj/" + path,
                        "obj/guns/" + path
                };

                for (String tryPath : pathsToTry) {
                    File modelFile = new File(modDir, tryPath);
                    DEBUG_PATHS.add("filesystem:" + modelFile.getAbsolutePath());
                    if (modelFile.exists()) {
                        ModularWarfare.LOGGER.info("  Found in filesystem: " + modelFile.getAbsolutePath());
                        return new FileInputStream(modelFile);
                    }
                }
            }
        } catch (Exception e) {
            // Игнорируем
        }

        return null;
    }

    private static AbstractObjModel parseObjFile(InputStream is, String originalPath) throws Exception {
        AbstractObjModel model = new AbstractObjModel();
        List<Vector3f> vertices = new ArrayList<>();
        List<float[]> texCoords = new ArrayList<>();

        Map<String, List<int[]>> partFaces = new HashMap<>();

        String currentPart = "gunModel";
        partFaces.put(currentPart, new ArrayList<>());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int lineNum = 0;
            int vertexCount = 0;
            int texCoordCount = 0;
            int faceCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "v":
                        if (parts.length >= 4) {
                            try {
                                float x = Float.parseFloat(parts[1]);
                                float y = Float.parseFloat(parts[2]);
                                float z = Float.parseFloat(parts[3]);
                                vertices.add(new Vector3f(x, y, z));
                                vertexCount++;
                            } catch (NumberFormatException e) {
                                // Игнорируем невалидные вершины
                            }
                        }
                        break;

                    case "vt":
                        if (parts.length >= 3) {
                            try {
                                float u = Float.parseFloat(parts[1]);
                                float v = 1.0f - Float.parseFloat(parts[2]); // Инвертируем V для Minecraft
                                texCoords.add(new float[]{u, v});
                                texCoordCount++;
                            } catch (NumberFormatException e) {
                                // Игнорируем
                            }
                        }
                        break;

                    case "vn":
                        // Пропускаем нормали - вычисляем сами
                        break;

                    case "f":
                        if (parts.length >= 4) {
                            List<Integer> faceVertices = new ArrayList<>();

                            for (int i = 1; i < parts.length; i++) {
                                String[] indices = parts[i].split("/");
                                try {
                                    int vIdx = Integer.parseInt(indices[0]) - 1;
                                    if (vIdx >= 0 && vIdx < vertices.size()) {
                                        faceVertices.add(vIdx);
                                    }
                                } catch (NumberFormatException e) {
                                    // Игнорируем
                                }
                            }

                            if (faceVertices.size() >= 3) {
                                // Триангуляция полигонов
                                for (int i = 0; i < faceVertices.size() - 2; i++) {
                                    int[] triangle = new int[3];
                                    triangle[0] = faceVertices.get(0);
                                    triangle[1] = faceVertices.get(i + 1);
                                    triangle[2] = faceVertices.get(i + 2);
                                    partFaces.get(currentPart).add(triangle);
                                    faceCount++;
                                }
                            }
                        }
                        break;

                    case "g":
                    case "o":
                        if (parts.length > 1) {
                            currentPart = parts[1];
                            if (!partFaces.containsKey(currentPart)) {
                                partFaces.put(currentPart, new ArrayList<>());
                                ModularWarfare.LOGGER.info("  Found part: " + currentPart);
                            }
                        }
                        break;
                }
            }

            ModularWarfare.LOGGER.info("  Parsed: " + vertexCount + " vertices, " +
                    texCoordCount + " texCoords, " + faceCount + " faces");
        }

        // Создаём рендереры для каждой части
        for (Map.Entry<String, List<int[]>> entry : partFaces.entrySet()) {
            String partName = entry.getKey();
            List<int[]> partFaceList = entry.getValue();

            if (partFaceList.isEmpty()) continue;

            ObjModelRenderer renderer = new ObjModelRenderer(partName);
            renderer.setVertices(vertices);
            renderer.setFaces(partFaceList);

            if (!texCoords.isEmpty()) {
                renderer.setTexCoords(texCoords);
            }

            model.addPart(partName, renderer);
        }

        // Если частей нет, создаём дефолтную
        if (model.getParts().isEmpty() && !vertices.isEmpty()) {
            ObjModelRenderer renderer = new ObjModelRenderer("gunModel");
            renderer.setVertices(vertices);
            renderer.setFaces(partFaces.get("gunModel"));
            if (!texCoords.isEmpty()) {
                renderer.setTexCoords(texCoords);
            }
            model.addPart("gunModel", renderer);
        }

        return model;
    }

    private static AbstractObjModel createEmptyModel() {
        ModularWarfare.LOGGER.warn("Creating empty model as fallback");
        AbstractObjModel model = new AbstractObjModel();
        ObjModelRenderer renderer = new ObjModelRenderer("gunModel");
        renderer.setVertices(new ArrayList<>());
        renderer.setFaces(new ArrayList<>());
        model.addPart("gunModel", renderer);
        return model;
    }

    public static void clearCache() {
        MODEL_CACHE.clear();
        DEBUG_PATHS.clear();
    }
}