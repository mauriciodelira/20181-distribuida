export const parseJsonWithQuotes = (str, fn) => {
  return (JSON.parse(
    str.replace(/\\"/g, '"'),
    fn
  ))
}