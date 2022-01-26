# BizHolidayCalc

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
-> And further into Item being dropped off before, during and after business hours.

+----------------------------+
| TestBusinessHourCalculator |
+----------------------------+
-> Various Unit Tests.

+---------+
| Remarks |
+---------+
-> calculateDeadline method included a lot of repetitive code, so I created a CurrentDateTime helper class, im sure
    there is a better way of doing this. Any feedback is appreciated.
-> Assignment stated 24-hour format but one time is given at 6:45 and not 06:45. I corrected this but can add a parser
    to include alternative times.
