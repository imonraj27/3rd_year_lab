#! /bin/bash

takeDate(){
    user_date=$1

    # EXTRACTING DAY MONTH AND YEAR
    dd=${user_date:0:2}
    mm=${user_date:3:2}
    yy=${user_date:6:4}

    # ECHOING IN UNIX FORMAT
    date -d "$yy-$mm-$dd" +%A
}

# TAKING THE TWO BITHDAYS
read -p "Enter first bithday: " date1
read -p "Enter second bithday: " date2

# TAKING THE DAY OF THE WEEK FROM THE FUNCTION
day1=$(takeDate $date1)
day2=$(takeDate $date2)

# CHECKING WHETHER BOTH DAYS ARE SAME OR NOT
if [ $day1 = $day2 ]; then
    echo "wow . same day - $day1"
else
    echo "Different day - $day1 and $day2"
fi


