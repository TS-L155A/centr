package com.ts2.centr.controllers;

import com.ts2.centr.models.Havka;
import com.ts2.centr.repo.HavkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private HavkaRepository havkaRepository;

    // общая папка для загрузок рядом с проектом
    private final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    // список
    @GetMapping
    public String blogMain(Model model) {
        Iterable<Havka> havkas = havkaRepository.findAll();
        model.addAttribute("havkas", havkas);
        return "blog-main";
    }

    // форма добавления
    @GetMapping("/add")
    public String blogAdd() {
        return "blog-add";
    }

    // сохранение новой записи — сначала файл, потом запись в БД
    @PostMapping("/add")
    public String blogPostAdd(
            @RequestParam String title,
            @RequestParam String addits,
            @RequestParam String unit,
            @RequestParam double quantity,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        String imagePath = null;

        // если файл передали — сохраняем
        if (file != null && !file.isEmpty()) {
            // гарантия: папка существует
            File uploadFolder = new File(UPLOAD_DIR);
            if (!uploadFolder.exists()) {
                boolean ok = uploadFolder.mkdirs();
                if (!ok) throw new IOException("Не удалось создать папку для загрузок: " + UPLOAD_DIR);
            }

            // безопасное имя файла
            String original = StringUtils.cleanPath(file.getOriginalFilename());
            if (original == null || original.isBlank()) original = "file";
            // заменяем пробелы и опасные символы на _
            String safe = original.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0,8) + "_" + safe;

            File dest = new File(uploadFolder, fileName);
            try {
                file.transferTo(dest); // сначала записываем файл
            } catch (IOException e) {
                throw new IOException("Ошибка при сохранении файла: " + fileName, e);
            }

            imagePath = "/uploads/" + fileName;
        }

        // теперь создаём объект и сохраняем в БД
        Havka havka = new Havka(title, addits, unit, imagePath, quantity);
        try {
            havkaRepository.save(havka);
        } catch (Exception e) {
            // если DB упала, удалим файл (если он был записан), чтобы не оставлять мусор
            if (imagePath != null) {
                File written = new File(UPLOAD_DIR, imagePath.substring("/uploads/".length()));
                if (written.exists()) written.delete();
            }
            throw e;
        }

        return "redirect:/blog";
    }

    // детали
    @GetMapping("/{id}")
    public String blogDetails(@PathVariable Long id, Model model) {
        Optional<Havka> havka = havkaRepository.findById(id);
        if (havka.isEmpty()) {
            return "error/404";
        }
        model.addAttribute("havka", havka.get());
        return "blog-details";
    }

    // форма редактирования
    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable Long id, Model model) {
        Optional<Havka> havka = havkaRepository.findById(id);
        if (havka.isEmpty()) {
            return "error/404";
        }
        model.addAttribute("havka", havka.get());
        return "blog-edit";
    }

    // сохранение изменений (можно загрузить новую картинку)
    @PostMapping("/{id}/edit")
    public String blogPostUpdate(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String addits,
            @RequestParam String unit,
            @RequestParam double quantity,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Havka havka = havkaRepository.findById(id).orElseThrow();

        String oldImage = havka.getImagePath();
        String newImagePath = null;

        if (file != null && !file.isEmpty()) {
            File uploadFolder = new File(UPLOAD_DIR);
            if (!uploadFolder.exists()) {
                boolean ok = uploadFolder.mkdirs();
                if (!ok) throw new IOException("Не удалось создать папку для загрузок: " + UPLOAD_DIR);
            }

            String original = StringUtils.cleanPath(file.getOriginalFilename());
            if (original == null || original.isBlank()) original = "file";
            String safe = original.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0,8) + "_" + safe;

            File dest = new File(uploadFolder, fileName);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new IOException("Ошибка при сохранении файла: " + fileName, e);
            }
            newImagePath = "/uploads/" + fileName;
            havka.setImagePath(newImagePath);
        }

        havka.setTitle(title);
        havka.setAddits(addits);
        havka.setUnit(unit);
        havka.setQuantity(quantity);

        try {
            havkaRepository.save(havka);
            // если новая картинка успешно сохранена в DB, удалим старый файл (если он был в uploads)
            if (newImagePath != null && oldImage != null && oldImage.startsWith("/uploads/")) {
                File oldFile = new File(UPLOAD_DIR, oldImage.substring("/uploads/".length()));
                if (oldFile.exists()) oldFile.delete();
            }
        } catch (Exception e) {
            // если DB упала — удалить только что записанный новый файл, если есть
            if (newImagePath != null) {
                File written = new File(UPLOAD_DIR, newImagePath.substring("/uploads/".length()));
                if (written.exists()) written.delete();
            }
            throw e;
        }

        return "redirect:/blog";
    }

    // удаление
    @PostMapping("/{id}/remove")
    public String blogPostDelete(@PathVariable Long id) {
        havkaRepository.findById(id).ifPresent(h -> {
            // удаляем файл с диска, если он лежит в uploads
            String img = h.getImagePath();
            if (img != null && img.startsWith("/uploads/")) {
                File f = new File(UPLOAD_DIR, img.substring("/uploads/".length()));
                if (f.exists()) f.delete();
            }
            havkaRepository.delete(h);
        });
        return "redirect:/blog";
    }
}
