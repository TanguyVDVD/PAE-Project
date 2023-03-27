const formatDate = (date) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(date).toLocaleDateString('fr-BE', options);
};

const formatPhoneNumber = (phoneNumber) => {
  let num = `${phoneNumber.slice(0, phoneNumber.length - 6)}/`;
  num += phoneNumber
    .slice(phoneNumber.length - 6)
    .match(/.{2}/g)
    .join('.');

  return num;
};

export { formatDate, formatPhoneNumber };
