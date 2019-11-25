package com.github.hicolors.best.practices.rest;

import com.github.hicolors.best.practices.exception.ExtensionException;
import com.github.hicolors.best.practices.model.ValidatedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * RestController
 *
 * @author Weichao Li (liweichao0102@gmail.com)
 * @since 2019/11/25
 */
@RestController
@RequestMapping("/simples")
public class SimpleRest {

    @GetMapping
    public String get() {
        throw new ExtensionException(105001001L, "simple 资源不存在");
    }

    @PostMapping("/test/validated")
    public String getx(@Validated @RequestBody ValidatedModel model) {
        return model.getName();
    }
}