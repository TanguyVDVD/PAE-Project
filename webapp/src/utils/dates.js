function subtractDates(date1, date2){
  date1.setHours(12,0,0,0)
  date2.setHours(12,0,0,0)
  const diffTime = Math.abs(date2.getTime() - date1.getTime());
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

export {
  subtractDates,
}