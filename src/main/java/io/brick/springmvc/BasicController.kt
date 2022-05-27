package io.brick.springmvc

import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/basic")
class BasicController {

    @GetMapping("/text-basic")
    fun textBasic(model: Model): String {
        model.addAttribute("data", "Hello <b>Spring!</b>")
        return "basic/text-basic"
    }

    @GetMapping("/text-unescaped")
    fun textUnescaped(model: Model): String {
        model.addAttribute("data", "Hello <b>Spring!</b>")
        return "basic/text-unescaped"
    }

    @GetMapping("/variable")
    fun variable(model: Model): String {
        val userA = User("userA", 10)
        val userB = User("userB", 20)

        val list = listOf(userA, userB)

        val map = mapOf("userA" to userA, "userB" to userB)

        model.addAttribute("user", userA)
        model.addAttribute("users", list)
        model.addAttribute("userMap", map)

        return "basic/variable"
    }

    @GetMapping("/basic-objects")
    fun basicObjects(session: HttpSession): String {
        session.setAttribute("sessionData", "Hello Session")

        return "basic/basic-objects"
    }

    @GetMapping("/date")
    fun date(model: Model): String {
        model.addAttribute("localDateTime", LocalDateTime.now())
        return "basic/date"
    }

    @GetMapping("/link")
    fun link(model: Model): String {
        model.addAttribute("param1", "data1")
        model.addAttribute("param2", "data2")

        return "basic/link"
    }

    @GetMapping("/literal")
    fun literal(model: Model): String {
        model.addAttribute("data", "Spring!")
        return "basic/literal"
    }

    @GetMapping("/operation")
    fun operation(model: Model): String {
        model.addAttribute("nullData", null)
        model.addAttribute("data", "Spring!")

        return "basic/operation"
    }

    @GetMapping("/attribute")
    fun attribute(): String {
        return "basic/attribute"
    }

    @GetMapping("/each")
    fun each(model: Model): String {
        addUsers(model)
        return "basic/each"
    }

    @GetMapping("/condition")
    fun condition(model: Model): String {
        addUsers(model)
        return "basic/condition"
    }

    @GetMapping("/comments")
    fun comments(model: Model): String {
        model.addAttribute("data", "Spring!")
        return "basic/comments"
    }

    @GetMapping("/block")
    fun block(model: Model): String {
        addUsers(model)
        return "basic/block"
    }

    @GetMapping("/javascript")
    fun javascript(model: Model): String {
        model.addAttribute("user", User("userA", 10))
        addUsers(model)

        return "basic/javascript"
    }

    private fun addUsers(model: Model) {
        val list = listOf(
            User("UserA", 10),
            User("UserB", 20),
            User("UserC", 30)
        )

        model.addAttribute("users", list)
    }
}

data class User(
    var username: String,
    var age: Int
)

@Component("helloBean")
class HelloBean {
    fun hello(data: String): String {
        return "Hello $data"
    }
}