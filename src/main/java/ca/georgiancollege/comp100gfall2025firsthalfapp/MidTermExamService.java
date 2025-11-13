package ca.georgiancollege.comp100gfall2025firsthalfapp;


import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class MidTermExamService {
    private static final String ROOT_PATH = Path.of("src", "main", "resources",
            ImageService.class.getPackage().getName().replace('.', '/')).toString();

    private static final String IMAGE_DIR = ROOT_PATH + "/images";
        private static final int DEFAULT_PER_PAGE = 10;

        private final String apiKey = "52784593-9dfd21e3ecaf02e140084e03f";

        public MidTermExamService() {
             try { Files.createDirectories(Path.of(IMAGE_DIR)); } catch (IOException ignored) {}
        }


        /**
         * Returns local file paths for up to `count` images for the given keyword.
         * Uses cache: images/<keyword>_1.jpg ... _N.jpg
         */
        public List<String> fetchAndCacheImages(String keyword, int count) throws IOException {
            // check cache first
            List<String> cached = getCachedImages(keyword, count);
            if (!cached.isEmpty()) return cached;

            // call Pixabay
            String q = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String urlStr = String.format(
                    "https://pixabay.com/api/?key=%s&q=%s&image_type=photo&per_page=%d&safesearch=true",
                    URLEncoder.encode(apiKey, StandardCharsets.UTF_8), q, Math.max(DEFAULT_PER_PAGE, count)
            );

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new IOException("Pixabay returned HTTP " + code);
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
            JsonArray hits = root.getAsJsonArray("hits");
            if (hits == null || hits.size() == 0) return Collections.emptyList();

            List<String> results = new ArrayList<>();
            int saved = 0;
            for (int i = 0; i < hits.size() && saved < count; i++) {
                JsonObject hit = hits.get(i).getAsJsonObject();
                // use webformatURL (smaller) or largeImageURL
                String imageUrl = null;
                if (hit.has("webformatURL")) imageUrl = hit.get("webformatURL").getAsString();
                else if (hit.has("largeImageURL")) imageUrl = hit.get("largeImageURL").getAsString();
                if (imageUrl == null || imageUrl.isBlank()) continue;

                Path dest = Path.of(IMAGE_DIR, sanitize(keyword) + "_" + (saved + 1) + ".jpg");
                boolean ok = downloadImage(imageUrl, dest);
                if (ok) {
                    results.add(dest.toString());
                    saved++;
                }
            }
            return results;
        }

        private List<String> getCachedImages(String keyword, int count) {
            List<String> files = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                Path p = Path.of(IMAGE_DIR, sanitize(keyword) + "_" + i + ".jpg");
                if (Files.exists(p)) files.add(p.toString());
            }
            return files;
        }

        private boolean downloadImage(String urlStr, Path dest) {
            try (InputStream in = new URL(urlStr).openStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException e) {
                System.err.println("Failed to download image: " + e.getMessage());
                return false;
            }
        }

        // simple filename-safe conversion
        private String sanitize(String s) {
            return s.toLowerCase().replaceAll("[^a-z0-9-_]", "_");
        }
    }

