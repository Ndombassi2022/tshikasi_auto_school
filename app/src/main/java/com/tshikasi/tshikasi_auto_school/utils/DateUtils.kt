package com.tshikasi.tshikasi_auto_school.utils

import kotlinx.datetime.*

/**
 * Utilitários para trabalhar com datas no app usando kotlinx.datetime
 */
object DateUtils {

    /**
     * Converte LocalDate para string formatada (dd/MM/yyyy)
     */
    fun formatDate(date: LocalDate): String {
        return "${date.dayOfMonth.toString().padStart(2, '0')}/" +
                "${date.monthNumber.toString().padStart(2, '0')}/" +
                "${date.year}"
    }

    /**
     * Converte LocalDate para string formatada (MMM yyyy)
     */
    fun formatMonthYear(date: LocalDate): String {
        val monthNames = listOf(
            "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
            "Jul", "Ago", "Set", "Out", "Nov", "Dez"
        )
        return "${monthNames[date.monthNumber - 1]} ${date.year}"
    }

    /**
     * Parseia string no formato ISO (yyyy-MM-dd)
     */
    fun parseIsoDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parseia string no formato brasileiro (dd/MM/yyyy)
     */
    fun parseBrazilianDate(dateString: String): LocalDate? {
        return try {
            val parts = dateString.split("/")
            if (parts.size != 3) return null
            LocalDate(
                year = parts[2].toInt(),
                monthNumber = parts[1].toInt(),
                dayOfMonth = parts[0].toInt()
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Retorna a data de hoje
     */
    fun today(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    /**
     * Calcula idade baseada na data de plantio
     */
    fun calculateAge(plantedDate: LocalDate): Int {
        val today = today()
        var age = today.year - plantedDate.year

        // Ajusta se ainda não fez "aniversário" este ano
        if (today.monthNumber < plantedDate.monthNumber ||
            (today.monthNumber == plantedDate.monthNumber && today.dayOfMonth < plantedDate.dayOfMonth)
        ) {
            age--
        }

        return age.coerceAtLeast(0)
    }

    /**
     * Formata período entre duas datas
     */
    fun formatDateRange(start: LocalDate, end: LocalDate): String {
        return "${formatDate(start)} - ${formatDate(end)}"
    }

    /**
     * Verifica se uma data é válida
     */
    fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        return try {
            LocalDate(year, month, day)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Retorna o primeiro dia do mês
     */
    fun getFirstDayOfMonth(date: LocalDate): LocalDate {
        return LocalDate(date.year, date.monthNumber, 1)
    }

    /**
     * Retorna o último dia do mês
     */
    fun getLastDayOfMonth(date: LocalDate): LocalDate {
        val daysInMonth = when (date.monthNumber) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(date.year)) 29 else 28
            else -> 30
        }
        return LocalDate(date.year, date.monthNumber, daysInMonth)
    }

    /**
     * Verifica se é ano bissexto
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * Adiciona dias a uma data
     */
    fun addDays(date: LocalDate, days: Int): LocalDate {
        val instant = date.atStartOfDayIn(TimeZone.UTC)
        val newInstant = instant.plus(days, DateTimeUnit.DAY, TimeZone.UTC)
        return newInstant.toLocalDateTime(TimeZone.UTC).date
    }

    /**
     * Adiciona meses a uma data
     */
    fun addMonths(date: LocalDate, months: Int): LocalDate {
        var newYear = date.year
        var newMonth = date.monthNumber + months

        while (newMonth > 12) {
            newMonth -= 12
            newYear++
        }
        while (newMonth < 1) {
            newMonth += 12
            newYear--
        }

        val maxDay = when (newMonth) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(newYear)) 29 else 28
            else -> 30
        }

        val newDay = date.dayOfMonth.coerceAtMost(maxDay)

        return LocalDate(newYear, newMonth, newDay)
    }

    /**
     * Adiciona anos a uma data
     */
    fun addYears(date: LocalDate, years: Int): LocalDate {
        return LocalDate(date.year + years, date.monthNumber, date.dayOfMonth)
    }

    /**
     * Calcula diferença em dias entre duas datas
     */
    fun daysBetween(start: LocalDate, end: LocalDate): Int {
        val startInstant = start.atStartOfDayIn(TimeZone.UTC)
        val endInstant = end.atStartOfDayIn(TimeZone.UTC)
        val duration = endInstant - startInstant
        return duration.inWholeDays.toInt()
    }
}

/**
 * Extension functions para LocalDate
 */
fun LocalDate.format(): String = DateUtils.formatDate(this)

fun LocalDate.formatMonthYear(): String = DateUtils.formatMonthYear(this)

fun LocalDate.addDays(days: Int): LocalDate = DateUtils.addDays(this, days)

fun LocalDate.addMonths(months: Int): LocalDate = DateUtils.addMonths(this, months)

fun LocalDate.addYears(years: Int): LocalDate = DateUtils.addYears(this, years)

fun LocalDate.isToday(): Boolean = this == DateUtils.today()

fun LocalDate.isFuture(): Boolean = this > DateUtils.today()

fun LocalDate.isPast(): Boolean = this < DateUtils.today()

fun LocalDate.age(): Int = DateUtils.calculateAge(this)