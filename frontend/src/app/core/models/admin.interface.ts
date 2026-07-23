export interface AdminResponse {
  id: string;
  email: string;
  username: string;
  firstName: string;
  middleName: string | null;
  paternalLastName: string;
  maternalLastName: string | null;
  dni: string;
  phone: string;
  birthDate: string;
  age: number;
  profile: string | null;
  gender: string;
  nationality: string;
  status: string;
  role: string;
}

export interface AdminRequest {
  email: string| null;
  username: string| null;
  password: string| null;
  firstName: string;
  middleName: string | null;
  paternalLastName: string;
  maternalLastName: string | null;
  dni: string;
  phone: string;
  birthDate: string;
  gender: string;
  nationality: string;
}

export interface AdminReportRequest {
  email: boolean;
  username: boolean;
  name: boolean;
  lastName: boolean;
  phone: boolean;
  dni: boolean;
  gender: boolean;
  status: boolean;
  statusFilter?: string;
}
