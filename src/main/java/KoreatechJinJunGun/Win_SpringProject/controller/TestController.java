package KoreatechJinJunGun.Win_SpringProject.controller;

import KoreatechJinJunGun.Win_SpringProject.entity.TestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<TestEntity> test(@RequestBody TestEntity kk){
        log.info("test controller");
        return ResponseEntity.status(HttpStatus.OK).body(kk);
    }
}
