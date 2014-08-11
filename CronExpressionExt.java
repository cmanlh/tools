import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import org.quartz.CronExpression;

/**
 * Extends the Quartz's CronExpression class
 * 
 * @author EX-LUHONG001
 * 
 */
public class CronExpressionExt extends CronExpression {

	private static final long serialVersionUID = -4200515728690443398L;

	public CronExpressionExt(String cronExpression) throws ParseException {
		super(cronExpression);
	}

	@SuppressWarnings("unchecked")
	public Date getLastFireTimeOf(Date time) {
		Calendar cl = new java.util.GregorianCalendar(getTimeZone());
		cl.setTime(time);

		// CronTrigger does not deal with milliseconds
		cl.set(Calendar.MILLISECOND, 0);

		// loop until we've computed the next time, or we've past the endTime
		boolean gotOne = false;
		while (!gotOne) {
			if (cl.get(Calendar.YEAR) < 1972) { // prevent endless loop
				return null;
			}

			SortedSet<Integer> st = null;
			int t = -1;

			// get year
			int year = cl.get(Calendar.YEAR);
			st = years.headSet(Integer.valueOf(year), true);
			if (st != null && st.size() != 0) {
				t = year;
				year = st.last().intValue();
			} else {
				return null; // ran out of years
			}

			if (t != year) {
				// set year
				cl.set(Calendar.YEAR, year);

				// set month
				setMonthForGetLastFireTime(cl);

				// set day
				setDayForGetLastFireTime(cl);

				// set Hour
				setHourForGetLastFireTime(cl);

				// set Minute
				setMinuteForGetLastFireTime(cl);

				// set second
				setSecondForGetLastFireTime(cl);

				return cl.getTime();
			}

			// get month
			int month = cl.get(Calendar.MONTH) + 1;
			st = months.headSet(Integer.valueOf(month), true);
			t = -1;

			if (null != st && st.size() != 0) {
				t = month;
				month = st.last().intValue();
			} else {
				month = ((Integer) months.headSet(13, true).last()).intValue();
				year--;
			}

			if (t != month) {
				cl.set(Calendar.SECOND, 59);
				cl.set(Calendar.MINUTE, 59);
				cl.set(Calendar.HOUR_OF_DAY, 23);
				cl.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month, cl.get(Calendar.YEAR)));
				// '- 1' because calendar is 0-based for this field, and we are 1-based
				cl.set(Calendar.MONTH, month - 1);
				cl.set(Calendar.YEAR, year);

				continue;
			}
			cl.set(Calendar.MONTH, month - 1);

			// get day
			month = cl.get(Calendar.MONTH) + 1;
			int tMonth = month;
			t = -1;

			int day = cl.get(Calendar.DAY_OF_MONTH);
			boolean notSpecMonth = daysOfMonth.contains(NO_SPEC);
			boolean notSpecWeek = daysOfWeek.contains(NO_SPEC);
			if (!notSpecMonth && notSpecWeek) {
				st = daysOfMonth.headSet(Integer.valueOf(day), true);

				if (lastdayOfMonth) {
					if (!nearestWeekday) {
						t = day;
						day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
						if (t < day) {
							month--;
							if (month < 1) {
								month = 12;
								tMonth = 9999;
								cl.set(Calendar.MONTH, -1);
							}
							day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
						}
					} else {
						t = day;
						day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));

						Calendar tcal = Calendar.getInstance(getTimeZone());
						tcal.set(Calendar.SECOND, 59);
						tcal.set(Calendar.MINUTE, 59);
						tcal.set(Calendar.HOUR_OF_DAY, 23);
						tcal.set(Calendar.DAY_OF_MONTH, day);
						tcal.set(Calendar.MONTH, month - 1);
						tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

						int dayOfWeek = tcal.get(Calendar.DAY_OF_WEEK);
						if (Calendar.SUNDAY == dayOfWeek) {
							day -= 2;
						} else if (Calendar.SATURDAY == dayOfWeek) {
							day -= 1;
						}
					}
				} else if (nearestWeekday) {
					t = day;
					day = ((Integer) daysOfMonth.last()).intValue();

					Calendar tcal = Calendar.getInstance(getTimeZone());
					tcal.set(Calendar.SECOND, 59);
					tcal.set(Calendar.MINUTE, 59);
					tcal.set(Calendar.HOUR_OF_DAY, 23);
					tcal.set(Calendar.DAY_OF_MONTH, day);
					tcal.set(Calendar.MONTH, month - 1);
					tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

					int dayOfWeek = tcal.get(Calendar.DAY_OF_WEEK);

					if (Calendar.SUNDAY == dayOfWeek && day > 2) {
						day -= 2;
					} else if (Calendar.SUNDAY == dayOfWeek) {
						day += 1;
					} else if (Calendar.SATURDAY == dayOfWeek && day > 1) {
						day -= 1;
					} else if (Calendar.SATURDAY == dayOfWeek) {
						day += 2;
					}
				} else if (st != null && st.size() != 0) {
					t = day;
					day = st.last().intValue();
					// make sure we don't over-run a short month, such as february
					int lastDay = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
					if (day > lastDay) {
						day = ((Integer) daysOfMonth.last()).intValue();
						month--;
					}
				} else {
					day = ((Integer) daysOfMonth.headSet(31, true).last()).intValue();
					month--;
				}

				if (day != t || month != tMonth) {
					cl.set(Calendar.SECOND, 59);
					cl.set(Calendar.MINUTE, 59);
					cl.set(Calendar.HOUR_OF_DAY, 23);
					cl.set(Calendar.DAY_OF_MONTH, day);
					// '- 1' because calendar is 0-based for this field, and we are 1-based
					cl.set(Calendar.MONTH, month - 1);
					continue;
				}
			} else if (!notSpecWeek && notSpecMonth) {
				if (lastdayOfWeek) { // are we looking for the last XXX day of the month?
					int dayOfWeek = ((Integer) daysOfWeek.last()).intValue(); // desired day of week

					int lastDayofMonth = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
					Calendar tcal = Calendar.getInstance(getTimeZone());
					tcal.set(Calendar.SECOND, 59);
					tcal.set(Calendar.MINUTE, 59);
					tcal.set(Calendar.HOUR_OF_DAY, 23);
					tcal.set(Calendar.DAY_OF_MONTH, lastDayofMonth);
					tcal.set(Calendar.MONTH, month - 1);
					tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
					int lastDOWOfMonth = cl.get(Calendar.DAY_OF_WEEK);

					int targetDayOfMonth = -1;
					if (lastDOWOfMonth > dayOfWeek) {
						targetDayOfMonth = lastDayofMonth - (lastDOWOfMonth - dayOfWeek);
					} else if (lastDOWOfMonth < dayOfWeek) {
						targetDayOfMonth = lastDayofMonth - (lastDOWOfMonth + 7 - dayOfWeek);
					} else {
						targetDayOfMonth = lastDayofMonth;
					}

					if (targetDayOfMonth > day) {
						t = day;
						month--;
						day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
					} else if (targetDayOfMonth < day) {
						t = day;
						day = targetDayOfMonth;
					}

					if (t != day) {
						cl.set(Calendar.SECOND, 59);
						cl.set(Calendar.MINUTE, 59);
						cl.set(Calendar.HOUR_OF_DAY, 23);
						cl.set(Calendar.DAY_OF_MONTH, day);
						// '- 1' here because we are not promoting the month
						cl.set(Calendar.MONTH, month - 1);
						continue;
					}
				} else if (nthdayOfWeek != 0) {
					// are we looking for the Nth XXX day in the month?
					int dayOfWeek = ((Integer) daysOfWeek.last()).intValue(); // desired day of week

					Calendar tcal = Calendar.getInstance(getTimeZone());
					tcal.set(Calendar.SECOND, 59);
					tcal.set(Calendar.MINUTE, 59);
					tcal.set(Calendar.HOUR_OF_DAY, 23);
					tcal.set(Calendar.DAY_OF_MONTH, day);
					tcal.set(Calendar.MONTH, month - 1);
					tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
					tcal.set(Calendar.WEEK_OF_MONTH, nthdayOfWeek);
					tcal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

					if ((tcal.get(Calendar.MONTH) + 1) != month) {
						t = day;
						month--;
						day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
					} else {
						t = day;
						day = tcal.get(Calendar.DAY_OF_MONTH);
						if (day > t) {
							month--;
							day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
						}
					}

					if (t != day) {
						cl.set(Calendar.SECOND, 59);
						cl.set(Calendar.MINUTE, 59);
						cl.set(Calendar.HOUR_OF_DAY, 23);
						cl.set(Calendar.DAY_OF_MONTH, day);
						// '- 1' here because we are not promoting the month
						cl.set(Calendar.MONTH, month - 1);
						continue;
					}
				} else {
					int currentDayOfWeek = cl.get(Calendar.DAY_OF_WEEK); // current day of week
					int dayOfWeek = ((Integer) daysOfWeek.headSet(7, true).last()).intValue(); // desired day of week
					st = daysOfWeek.headSet(Integer.valueOf(currentDayOfWeek), true);
					if (st != null && st.size() > 0) {
						dayOfWeek = st.last().intValue();
					}

					int daysToMinus = 0;
					if (dayOfWeek < currentDayOfWeek) {
						daysToMinus = currentDayOfWeek - dayOfWeek;
					} else if (dayOfWeek > currentDayOfWeek) {
						daysToMinus = currentDayOfWeek + 7 - dayOfWeek;
					}

					if (day - daysToMinus > 1) {
						day -= daysToMinus;
					} else {
						cl.set(Calendar.SECOND, 59);
						cl.set(Calendar.MINUTE, 59);
						cl.set(Calendar.HOUR_OF_DAY, 23);
						cl.set(Calendar.DAY_OF_MONTH, day - daysToMinus);
						continue;
					}
				}
			}
			cl.set(Calendar.DAY_OF_MONTH, day);

			// get hour
			int hour = cl.get(Calendar.HOUR_OF_DAY);
			day = cl.get(Calendar.DAY_OF_MONTH);
			t = -1;
			st = hours.headSet(Integer.valueOf(hour), true);
			if (st != null && st.size() != 0) {
				t = hour;
				hour = st.last().intValue();
			} else {
				hour = ((Integer) hours.last()).intValue();
				day--;
			}

			if (hour != t) {
				cl.set(Calendar.SECOND, 59);
				cl.set(Calendar.MINUTE, 59);
				cl.set(Calendar.DAY_OF_MONTH, day);
				setCalendarHour(cl, hour);
				continue;
			}
			cl.set(Calendar.HOUR_OF_DAY, hour);

			// get minute
			int minute = cl.get(Calendar.MINUTE);
			hour = cl.get(Calendar.HOUR_OF_DAY);
			t = -1;
			st = minutes.headSet(Integer.valueOf(minute), true);
			if (st != null && st.size() != 0) {
				t = minute;
				minute = st.last().intValue();
			} else {
				minute = ((Integer) minutes.first()).intValue();
				hour--;
			}
			if (minute != t) {
				cl.set(Calendar.SECOND, 59);
				cl.set(Calendar.MINUTE, minute);
				setCalendarHour(cl, hour);
				continue;
			}
			cl.set(Calendar.MINUTE, minute);

			// get second.................................................
			t = 0;
			int second = cl.get(Calendar.SECOND);
			minute = cl.get(Calendar.MINUTE);
			st = seconds.headSet(Integer.valueOf(second), true);
			if (st != null && st.size() != 0) {
				second = st.last().intValue();
			} else {
				second = ((Integer) seconds.last()).intValue();
				minute--;
				cl.set(Calendar.MINUTE, minute);
			}
			cl.set(Calendar.SECOND, second);

			gotOne = true;

		}

		return cl.getTime();
	}

	@SuppressWarnings("unchecked")
	private void setSecondForGetLastFireTime(Calendar cl) {
		SortedSet<Integer> st = seconds.headSet(Integer.valueOf(59), true);

		if (null == st || st.size() == 0) {
			cl.set(Calendar.SECOND, 59);
		} else {
			cl.set(Calendar.SECOND, st.last().intValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void setMinuteForGetLastFireTime(Calendar cl) {
		SortedSet<Integer> st = minutes.headSet(Integer.valueOf(59), true);

		if (null == st || st.size() == 0) {
			cl.set(Calendar.MINUTE, 59);
		} else {
			cl.set(Calendar.MINUTE, st.last().intValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void setHourForGetLastFireTime(Calendar cl) {
		SortedSet<Integer> st = hours.headSet(Integer.valueOf(23), true);

		if (null == st || st.size() == 0) {
			cl.set(Calendar.HOUR, 23);
		} else {
			cl.set(Calendar.HOUR, st.last().intValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void setDayForGetLastFireTime(Calendar cl) {
		SortedSet<Integer> st = null;
		int t = -1, month = cl.get(Calendar.MONTH) + 1, tMonth = -1;
		cl.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month, cl.get(Calendar.YEAR)));
		int day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));

		boolean notSpecMonth = daysOfMonth.contains(NO_SPEC);
		boolean notSpecWeek = daysOfWeek.contains(NO_SPEC);
		if (!notSpecMonth && notSpecWeek) {
			st = daysOfMonth.headSet(Integer.valueOf(getLastDayOfMonth(month, cl.get(Calendar.YEAR))), true);

			if (lastdayOfMonth) {
				if (!nearestWeekday) {
					day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
				} else {
					day = getLastDayOfMonth(month, cl.get(Calendar.YEAR));

					Calendar tcal = Calendar.getInstance(getTimeZone());
					tcal.set(Calendar.SECOND, 59);
					tcal.set(Calendar.MINUTE, 59);
					tcal.set(Calendar.HOUR_OF_DAY, 23);
					tcal.set(Calendar.DAY_OF_MONTH, day);
					tcal.set(Calendar.MONTH, month - 1);
					tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

					int dayOfWeek = tcal.get(Calendar.DAY_OF_WEEK);
					if (Calendar.SUNDAY == dayOfWeek) {
						day -= 2;
					} else if (Calendar.SATURDAY == dayOfWeek) {
						day -= 1;
					}
				}
			} else if (nearestWeekday) {
				t = day;
				day = ((Integer) daysOfMonth.headSet(getLastDayOfMonth(month, cl.get(Calendar.YEAR)), true).last()).intValue();

				Calendar tcal = Calendar.getInstance(getTimeZone());
				tcal.set(Calendar.SECOND, 59);
				tcal.set(Calendar.MINUTE, 59);
				tcal.set(Calendar.HOUR_OF_DAY, 23);
				tcal.set(Calendar.DAY_OF_MONTH, day);
				tcal.set(Calendar.MONTH, month - 1);
				tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

				int dayOfWeek = tcal.get(Calendar.DAY_OF_WEEK);

				if (Calendar.SUNDAY == dayOfWeek && day > 2) {
					day -= 2;
				} else if (Calendar.SUNDAY == dayOfWeek) {
					day += 1;
				} else if (Calendar.SATURDAY == dayOfWeek && day > 1) {
					day -= 1;
				} else if (Calendar.SATURDAY == dayOfWeek) {
					day += 2;
				}
			} else if (st != null && st.size() != 0) {
				t = day;
				day = st.last().intValue();
				// make sure we don't over-run a short month, such as february
				int lastDay = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
				if (day > lastDay) {
					cl.set(Calendar.MONTH, month - 2);
					cl.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month - 2, cl.get(Calendar.YEAR)));
					setDayForGetLastFireTime(cl);

					return;
				}
			} else {
				cl.set(Calendar.MONTH, month - 2);
				cl.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month - 2, cl.get(Calendar.YEAR)));
				setDayForGetLastFireTime(cl);

				return;
			}
		} else if (!notSpecWeek && notSpecMonth) {
			if (lastdayOfWeek) {
				int dayOfWeek = ((Integer) daysOfWeek.last()).intValue(); // desired day of week

				int lastDayofMonth = getLastDayOfMonth(month, cl.get(Calendar.YEAR));
				Calendar tcal = Calendar.getInstance(getTimeZone());
				tcal.set(Calendar.SECOND, 59);
				tcal.set(Calendar.MINUTE, 59);
				tcal.set(Calendar.HOUR_OF_DAY, 23);
				tcal.set(Calendar.DAY_OF_MONTH, lastDayofMonth);
				tcal.set(Calendar.MONTH, month - 1);
				tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
				int lastDOWOfMonth = cl.get(Calendar.DAY_OF_WEEK);

				int targetDayOfMonth = -1;
				if (lastDOWOfMonth > dayOfWeek) {
					targetDayOfMonth = lastDayofMonth - (lastDOWOfMonth - dayOfWeek);
				} else if (lastDOWOfMonth < dayOfWeek) {
					targetDayOfMonth = lastDayofMonth - (lastDOWOfMonth + 7 - dayOfWeek);
				} else {
					targetDayOfMonth = lastDayofMonth;
				}

				day = targetDayOfMonth;
			} else if (nthdayOfWeek != 0) {
				// are we looking for the Nth XXX day in the month?
				int dayOfWeek = ((Integer) daysOfWeek.last()).intValue(); // desired day of week

				Calendar tcal = Calendar.getInstance(getTimeZone());
				tcal.set(Calendar.SECOND, 59);
				tcal.set(Calendar.MINUTE, 59);
				tcal.set(Calendar.HOUR_OF_DAY, 23);
				tcal.set(Calendar.DAY_OF_MONTH, day);
				tcal.set(Calendar.MONTH, month - 1);
				tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
				tcal.set(Calendar.WEEK_OF_MONTH, nthdayOfWeek);
				tcal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

				if ((tcal.get(Calendar.MONTH) + 1) != month) {
					cl.set(Calendar.MONTH, month - 2);
					cl.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month - 2, cl.get(Calendar.YEAR)));
					setDayForGetLastFireTime(cl);

					return;
				} else {
					day = tcal.get(Calendar.DAY_OF_MONTH);
				}
			} else {
				int currentDayOfWeek = cl.get(Calendar.DAY_OF_WEEK); // current day of week
				int dayOfWeek = ((Integer) daysOfWeek.headSet(7, true).last()).intValue(); // desired day of week
				st = daysOfWeek.headSet(Integer.valueOf(currentDayOfWeek), true);
				if (st != null && st.size() > 0) {
					dayOfWeek = st.last().intValue();
				}

				int daysToMinus = 0;
				if (dayOfWeek < currentDayOfWeek) {
					daysToMinus = currentDayOfWeek - dayOfWeek;
				} else if (dayOfWeek > currentDayOfWeek) {
					daysToMinus = currentDayOfWeek + 7 - dayOfWeek;
				}

				day -= daysToMinus;
			}
		}
		cl.set(Calendar.DAY_OF_MONTH, day);
	}

	@SuppressWarnings("unchecked")
	private void setMonthForGetLastFireTime(Calendar cl) {
		SortedSet<Integer> st = months.headSet(Integer.valueOf(12), true);

		if (null == st || st.size() == 0) {
			cl.set(Calendar.MONTH, Calendar.DECEMBER);
		} else {
			cl.set(Calendar.MONTH, ((Integer) st.last()).intValue() - 1);
		}
	}

	public static void main(String[] args) throws ParseException {
		CronExpressionExt exp = new CronExpressionExt("0 0 4 1,11,21 * ?");
		Calendar tcal = Calendar.getInstance();
		tcal.set(Calendar.DAY_OF_MONTH, -6);
		System.out.println(exp.getNextValidTimeAfter(tcal.getTime()));
		System.out.println(exp.getLastFireTimeOf(tcal.getTime()));

	}
}
