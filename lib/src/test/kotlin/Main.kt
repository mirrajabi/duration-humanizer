import nl.mirrajabi.humanize.duration.DurationHumanizer

fun main(args: Array<String>) {
    val humanizer = DurationHumanizer()

    val ms = DurationHumanizer.TimeUnit.MILLISECOND.milliseconds
    val s = DurationHumanizer.TimeUnit.SECOND.milliseconds
    val m = DurationHumanizer.TimeUnit.MINUTE.milliseconds
    val h = DurationHumanizer.TimeUnit.HOUR.milliseconds
    val d = DurationHumanizer.TimeUnit.DAY.milliseconds
    val w = DurationHumanizer.TimeUnit.WEEK.milliseconds
    val mo = DurationHumanizer.TimeUnit.MONTH.milliseconds
    val y = DurationHumanizer.TimeUnit.YEAR.milliseconds

    println("Units:")
    println(humanizer.humanize(ms))
    println(humanizer.humanize(s))
    println(humanizer.humanize(m))
    println(humanizer.humanize(h))
    println(humanizer.humanize(d))
    println(humanizer.humanize(w))
    println(humanizer.humanize(mo))
    println(humanizer.humanize(y))

    println()
    println(humanizer.humanize(s + ms))
    println(humanizer.humanize(m + s))
    println(humanizer.humanize(h + m))
    println(humanizer.humanize(d + h))
    println(humanizer.humanize(w + d))
    println(humanizer.humanize(mo + w))
    println(humanizer.humanize(y + mo))

    println()
    println(humanizer.humanize(s + ms * 2))
    println(humanizer.humanize(m + s * 2))
    println(humanizer.humanize(h + m * 2))
    println(humanizer.humanize(d + h * 2))
    println(humanizer.humanize(w + d * 2))
    println(humanizer.humanize(mo + w * 2))
    println(humanizer.humanize(y + mo * 2))

    println()
    val o1 = DurationHumanizer.Options(conjunction = " and ", serialComma = false, round = true)
    println(humanizer.humanize(m * 4 + s * 3 + ms * 2, o1))
    println(humanizer.humanize(h * 4 + m * 3 + s * 2, o1))
    println(humanizer.humanize(d * 4 + h * 3 + m * 2, o1))
    println(humanizer.humanize(w * 3 + d * 3 + h * 2, o1))
    println(humanizer.humanize(mo * 4 + w * 3 + d * 2, o1))
    println(humanizer.humanize(y * 4 + mo * 3 + w * 2, o1))
    println(humanizer.humanize(1600, o1))


    println()
    println(humanizer.humanize(3000))
    println(humanizer.humanize(2250))
    println(humanizer.humanize(97320000))
}
