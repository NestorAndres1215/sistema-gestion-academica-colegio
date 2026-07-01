export interface User {

  username: string;
  email: string;
  role: string;
}

export interface PasswordChange {
  currentPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}