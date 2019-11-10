package ren.crux.rainbow.demo.controller;

import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.demo.model.E1;
import ren.crux.rainbow.demo.model.E2;
import ren.crux.rainbow.demo.model.E3;

/**
 * C1
 * xxxx {@link E2} ax
 * xxx
 *
 * @author wangzhihui
 * @see E1
 */
@RestController
@RequestMapping("/c1")
public class C1 {

    /**
     * e1
     * <p>
     * xxxx
     *
     * @param e1 xasdasd
     * @return asdasd
     */
    @GetMapping("/e1")
    public E1 e1(E1 e1) {
        return e1;
    }

    /**
     * asdasd
     *
     * @param rs das
     * @param h  asda
     * @param s  asdasd
     * @return qweqw
     */
    @GetMapping("/e2")
    public E2 e2(@RequestParam String rs, @RequestHeader String h, String s, int i) {
        return new E2();
    }

    /**
     * asdasd
     *
     * @param e asd
     * @return asdas
     */
    @PostMapping("/e3")
    public E3 e3(@RequestBody E3 e) {
        return e;
    }

}
