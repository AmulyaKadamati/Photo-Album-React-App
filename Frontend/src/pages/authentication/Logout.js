const Logout = () => {
  // Clear the token from local storage

  localStorage.removeItem('token');

  window.location.reload();
  window.location.href = '/login';
};
export default Logout;
