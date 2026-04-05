package com.modularwarfare.loader.api;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.type.BaseType;
import org.joml.Vector3f;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ObjModelLoader {

    private static final Set<String> FOUND_PATHS = new HashSet<>();

    public static AbstractObjModel load(String path) {
        ModularWarfare.LOGGER.info("=== Loading OBJ: " + path + " ===");

        // Нормализуем путь
        String normalizedPath = normalizePath(path);

        // Список всех возможных источников
        InputStream is = null;
        String source = null;

        // 1. Пробуем из classpath (resources)
        is = tryLoadFromClasspath(normalizedPath);
        if (is != null) source = "classpath";

        // 2. Пробуем из assets
        if (is == null) {
            is = tryLoadFromAssets(normalizedPath);
            if (is != null) source = "assets";
        }

        // 3. Пробуем из файловой системы (ModularWarfare папка)
        if (is == null) {
            is = tryLoadFromFileSystem(normalizedPath);
            if (is != null) source = "filesystem";
        }

        // 4. Пробуем из JAR напрямую
        if (is == null) {
            is = tryLoadFromJar(normalizedPath);
            if (is != null) source = "jar";
        }

        if (is == null) {
            ModularWarfare.LOGGER.error("❌ OBJ file not found: " + path);
            ModularWarfare.LOGGER.error("   Tried paths: " + FOUND_PATHS);
            return new AbstractObjModel();
        }

        ModularWarfare.LOGGER.info("✅ Found OBJ at: " + source);

        try {
            AbstractObjModel model = parseObjFile(is, path);
            if (model.getParts().isEmpty()) {
                ModularWarfare.LOGGER.warn("⚠️ OBJ file parsed but no parts found: " + path);
            } else {
                ModularWarfare.LOGGER.info("✅ OBJ parsed successfully: " + model.getParts().size() + " parts");
            }
            return model;
        } catch (Exception e) {
            ModularWarfare.LOGGER.error("❌ Failed to parse OBJ: " + path, e);
            return new AbstractObjModel();
        } finally {
            try { is.close(); } catch (Exception e) {}
        }
    }

    public static AbstractObjModel load(BaseType type, String path) {
        return load(path);
    }

    private static String normalizePath(String path) {
        // Убираем начальные слэши
        while (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1);
        }
        // Убираем assets/ если есть в начале
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
        try {
            String[] pathsToTry = {
                    path,
                    "assets/" + path,
                    "assets/modularwarfare/" + path,
                    "obj/" + path,
                    "obj/guns/" + path,
                    "/" + path
            };

            for (String tryPath : pathsToTry) {
                FOUND_PATHS.add("classpath: " + tryPath);
                InputStream is = ObjModelLoader.class.getClassLoader().getResourceAsStream(tryPath);
                if (is != null) {
                    ModularWarfare.LOGGER.info("   Found in classpath: " + tryPath);
                    return is;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static InputStream tryLoadFromAssets(String path) {
        try {
            String[] pathsToTry = {
                    "assets/modularwarfare/" + path,
                    "assets/" + path,
                    path
            };

            for (String tryPath : pathsToTry) {
                FOUND_PATHS.add("assets: " + tryPath);
                InputStream is = ObjModelLoader.class.getClassLoader().getResourceAsStream(tryPath);
                if (is != null) {
                    ModularWarfare.LOGGER.info("   Found in assets: " + tryPath);
                    return is;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static InputStream tryLoadFromFileSystem(String path) {
        try {
            File modDir = ModularWarfare.MOD_DIR;
            if (modDir == null) {
                String userDir = System.getProperty("user.dir");
                modDir = new File(userDir, "ModularWarfare");
            }

            String[] pathsToTry = {
                    path,
                    "obj/" + path,
                    "obj/guns/" + path,
                    "models/" + path,
                    modDir.getAbsolutePath() + "/" + path,
                    modDir.getAbsolutePath() + "/obj/" + path,
                    modDir.getAbsolutePath() + "/obj/guns/" + path
            };

            for (String tryPath : pathsToTry) {
                File modelFile = new File(tryPath);
                FOUND_PATHS.add("filesystem: " + modelFile.getAbsolutePath());
                if (modelFile.exists()) {
                    ModularWarfare.LOGGER.info("   Found in filesystem: " + modelFile.getAbsolutePath());
                    return new FileInputStream(modelFile);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static InputStream tryLoadFromJar(String path) {
        try {
            URL jarUrl = ObjModelLoader.class.getProtectionDomain().getCodeSource().getLocation();
            if (jarUrl == null) return null;

            File jarFile = new File(jarUrl.toURI());
            if (!jarFile.exists()) return null;

            String[] pathsToTry = {
                    path,
                    "assets/" + path,
                    "assets/modularwarfare/" + path,
                    "obj/" + path,
                    "obj/guns/" + path
            };

            try (ZipFile zipFile = new ZipFile(jarFile)) {
                for (String tryPath : pathsToTry) {
                    FOUND_PATHS.add("jar: " + jarFile.getAbsolutePath() + "!/" + tryPath);
                    ZipEntry entry = zipFile.getEntry(tryPath);
                    if (entry != null && !entry.isDirectory()) {
                        ModularWarfare.LOGGER.info("   Found in JAR: " + tryPath);
                        return zipFile.getInputStream(entry);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static AbstractObjModel parseObjFile(InputStream is, String originalPath) throws Exception {
        AbstractObjModel model = new AbstractObjModel();
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<float[]> texCoords = new ArrayList<>();

        Map<String, List<int[]>> partFaces = new HashMap<>();
        Map<String, List<Vector3f>> partVertices = new HashMap<>();
        Map<String, List<Vector3f>> partNormals = new HashMap<>();
        Map<String, List<float[]>> partTexCoords = new HashMap<>();

        String currentPart = "gunModel";
        partFaces.put(currentPart, new ArrayList<>());
        partVertices.put(currentPart, new ArrayList<>());
        partNormals.put(currentPart, new ArrayList<>());
        partTexCoords.put(currentPart, new ArrayList<>());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int lineNum = 0;
            int vertexCount = 0;
            int faceCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "v": // Vertex
                        if (parts.length >= 4) {
                            try {
                                float x = Float.parseFloat(parts[1]);
                                float y = Float.parseFloat(parts[2]);
                                float z = Float.parseFloat(parts[3]);
                                Vector3f v = new Vector3f(x, y, z);
                                vertices.add(v);
                                partVertices.get(currentPart).add(v);
                                vertexCount++;
                            } catch (NumberFormatException e) {
                                ModularWarfare.LOGGER.warn("Invalid vertex at line " + lineNum);
                            }
                        }
                        break;

                    case "vn": // Vertex Normal
                        if (parts.length >= 4) {
                            float nx = Float.parseFloat(parts[1]);
                            float ny = Float.parseFloat(parts[2]);
                            float nz = Float.parseFloat(parts[3]);
                            normals.add(new Vector3f(nx, ny, nz));
                            partNormals.get(currentPart).add(new Vector3f(nx, ny, nz));
                        }
                        break;

                    case "vt": // Texture Coordinate
                        if (parts.length >= 3) {
                            float u = Float.parseFloat(parts[1]);
                            float v = Float.parseFloat(parts[2]);
                            texCoords.add(new float[]{u, v});
                            partTexCoords.get(currentPart).add(new float[]{u, v});
                        }
                        break;

                    case "f": // Face
                        if (parts.length >= 4) {
                            List<Integer> faceVertices = new ArrayList<>();
                            List<Integer> faceNormals = new ArrayList<>();
                            List<Integer> faceTexCoords = new ArrayList<>();

                            boolean valid = true;
                            for (int i = 1; i < parts.length; i++) {
                                String[] indices = parts[i].split("/");
                                try {
                                    // Vertex index (required)
                                    int vIdx = Integer.parseInt(indices[0]) - 1;
                                    if (vIdx >= 0 && vIdx < vertices.size()) {
                                        faceVertices.add(vIdx);
                                    } else {
                                        valid = false;
                                        break;
                                    }

                                    // Texture coordinate index (optional)
                                    if (indices.length > 1 && !indices[1].isEmpty()) {
                                        int vtIdx = Integer.parseInt(indices[1]) - 1;
                                        if (vtIdx >= 0 && vtIdx < texCoords.size()) {
                                            faceTexCoords.add(vtIdx);
                                        }
                                    }

                                    // Normal index (optional)
                                    if (indices.length > 2 && !indices[2].isEmpty()) {
                                        int vnIdx = Integer.parseInt(indices[2]) - 1;
                                        if (vnIdx >= 0 && vnIdx < normals.size()) {
                                            faceNormals.add(vnIdx);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid && faceVertices.size() >= 3) {
                                // Triangulate if needed (convert quads to triangles)
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

                    case "g": // Group
                    case "o": // Object
                        if (parts.length > 1) {
                            currentPart = parts[1];
                            if (!partFaces.containsKey(currentPart)) {
                                partFaces.put(currentPart, new ArrayList<>());
                                partVertices.put(currentPart, new ArrayList<>());
                                partNormals.put(currentPart, new ArrayList<>());
                                partTexCoords.put(currentPart, new ArrayList<>());
                                ModularWarfare.LOGGER.info("   Found part: " + currentPart);
                            }
                        }
                        break;

                    case "usemtl": // Material
                    case "mtllib":
                        // Пока игнорируем, но не логгируем как ошибку
                        break;
                }
            }

            ModularWarfare.LOGGER.info("   Parsed: " + vertexCount + " vertices, " + faceCount + " faces");
        }

        // Создаём рендереры для каждой части
        for (Map.Entry<String, List<int[]>> entry : partFaces.entrySet()) {
            String partName = entry.getKey();
            List<int[]> partFaceList = entry.getValue();

            if (partFaceList.isEmpty()) continue;

            ObjModelRenderer renderer = new ObjModelRenderer(partName);

            // Используем вершины этой части или глобальные
            List<Vector3f> partVertexList = partVertices.get(partName);
            if (partVertexList != null && !partVertexList.isEmpty()) {
                renderer.setVertices(partVertexList);
            } else {
                renderer.setVertices(vertices);
            }

            renderer.setFaces(partFaceList);
            model.addPart(partName, renderer);

            ModularWarfare.LOGGER.info("   Part '" + partName + "': " + partFaceList.size() + " faces");
        }

        // Если нет частей, создаём дефолтную
        if (model.getParts().isEmpty() && !vertices.isEmpty() && !partFaces.get("gunModel").isEmpty()) {
            ObjModelRenderer renderer = new ObjModelRenderer("gunModel");
            renderer.setVertices(vertices);
            renderer.setFaces(partFaces.get("gunModel"));
            model.addPart("gunModel", renderer);
            ModularWarfare.LOGGER.info("   Created default part 'gunModel' with " + partFaces.get("gunModel").size() + " faces");
        }

        return model;
    }

    public static void clearCache() {
        FOUND_PATHS.clear();
    }
}