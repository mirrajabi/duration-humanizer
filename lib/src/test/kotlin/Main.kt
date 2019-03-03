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
}
