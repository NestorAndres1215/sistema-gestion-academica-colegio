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

export interface UserModel {
  id: number;
  username: string;
  firstName: string;
  middleName: string | null;
  paternalLastName: string;
  maternalLastName: string | null;
  email: string;
  profile: string | null;
  role: string;
  status: 'ACTIVE' | 'INACTIVE';
}