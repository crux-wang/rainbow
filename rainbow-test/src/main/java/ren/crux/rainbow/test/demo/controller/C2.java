package ren.crux.rainbow.test.demo.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.test.demo.model.E1;
import ren.crux.rainbow.test.demo.model.E2;
import ren.crux.rainbow.test.demo.model.E3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * C2
 * xxxx {@link E3} ax
 * xxx
 *
 * @author wangzhihui
 * @see E1
 */
@RestController
@RequestMapping(path = {"/c2", "/c3"}, method = {RequestMethod.POST, RequestMethod.GET})
public class C2 {

    /**
     * e1
     * <p>
     * xxxx
     *
     * @param e1 xasdasd
     * @return asdasd
     */
    @GetMapping("/e1")
    public List<E1> e1(E1 e1) {
        return Collections.singletonList(e1);
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
    public Map<String, E2> e2(@RequestParam String rs, @RequestHeader String h, String s) {
        return Collections.singletonMap("e2", new E2());
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

    /**
     * pair
     *
     * @return asdasd
     */
    @GetMapping("/pair")
    public Pair<String, String> pair() {
        return Pair.of("xaxa", "asdasd");
    }
}
