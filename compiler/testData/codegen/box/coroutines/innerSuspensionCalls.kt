// WITH_RUNTIME
// WITH_COROUTINES
class Controller {
    var i = 0
    suspend fun suspendHere(): String = suspendWithCurrentContinuation { x ->
        x.resume((i++).toString())
        SUSPENDED
    }

    // INTERCEPT_RESUME_PLACEHOLDER
}

fun builder(c: @Suspend() (Controller.() -> Unit)) {
    c.startCoroutine(Controller(), EmptyContinuation)
}

fun box(): String {
    var result = ""

    builder {
        result += "-"
        result += suspendHere()

        if (result == "-0") {
            builder {
                result += "+"
                result += suspendHere()
                result += suspendHere()
                result += "#"
            }

            result += suspendHere()
            result += "&"
        }
    }

    if (result != "-0+01#1&") return "fail: $result"

    return "OK"
}