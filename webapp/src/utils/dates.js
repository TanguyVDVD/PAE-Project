function subtractDates(date1, date2){
  date1.setHours(12,0,0,0);
  date2.setHours(12,0,0,0);
  const diffTime = Math.abs(date2.getTime() - date1.getTime());
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

function dateStringtoGoodFormat(dateString){
  const date = new Date(dateString);
  const yyyy = String(date.getFullYear());
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return dd.concat("/", mm, "/", yyyy);
}

function getTodaySDate(){
  const date = new Date();
  const yyyy = String(date.getFullYear());
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return yyyy.concat("-", mm, "-", dd);
}

export {
    subtractDates,
    dateStringtoGoodFormat,
    getTodaySDate
}