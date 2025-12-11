export interface ProductoDTO {
  id?: number;
  codigo: string;
  nombre: string;
  precio: number;
  moneda?: string;
  caracteristicas?: string;
  empresaNit?: string;
  categoriaId?: number;
}

export interface EmpresaDTO {
  nit: string;
  nombre: string;
  direccion?: string;
  telefono?: string;
  productos?: ProductoDTO[];
}

export interface UsuarioDTO {
  id?: number;
  username: string;
  email?: string;
  role?: string;
  activo?: boolean;
}

export interface AuthRequest {
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  expiresIn: number;
  role: string;
  email: string;
}

export interface InventoryReportRequest {
  toEmail: string;
  subject: string;
  body?: string;
  empresaNit?: string;
}

// Generic API Response wrapper
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  USER = 'USER'
}