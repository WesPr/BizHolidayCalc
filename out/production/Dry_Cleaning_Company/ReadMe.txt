+---------------+
| DayOfWeekEnum |
+---------------+
-> Assigned values 1-7 for each weekday to allow for easy referencing between DayOfWeekEnum and DayOfWeek API.

+--------------+
| DayTimeRange |
+--------------+
-> Overloaded constructor allowing for the use of Enums and Strings.

+------------------------+
| BusinessHourCalculator |
+------------------------+
-> Using Time API as Date API is deprecated. I found that my date was correct debugging but incorrect using Date API
    and investigation on Stackoverflow advised that this is a common error.
-> Use of Hashmap to store days as keys with their respective time ranges.
-> Use of Set to exclude duplicate days on specific dates.
-> Overloaded setOpeningHours method allowing for the use of Enums and Strings.
-> Used Varargs for multiple arguments in setClosed methods.
-> Used While loop to return final completion time upon exhaustion of remaining seconds.
-> I have split it up into a "SPECIFIC DATES SECTION" & "WEEKDAY SECTION" for easy reading.

+----------------------------+
| TestBusinessHourCalculator |
+----------------------------+
-> Various Unit Tests.