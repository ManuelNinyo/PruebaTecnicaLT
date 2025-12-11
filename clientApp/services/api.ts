import { 
  AuthRequest, 
  AuthResponse, 
  ApiResponse, 
  EmpresaDTO, 
  ProductoDTO, 
  InventoryReportRequest,
  UsuarioDTO
} from '../types';

const API_URL = 'http://localhost:8080/api';

const getHeaders = () => {
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  };
  const token = localStorage.getItem('token');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
};

const handleResponse = async <T>(response: Response): Promise<T> => {
  if (response.status === 401) {
    localStorage.removeItem('token');
    window.location.hash = '#/login';
    throw new Error('Unauthorized');
  }
  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || 'API Error');
  }
  return data; // Assumes the API returns the ApiResponse wrapper
};

export const api = {
  auth: {
    login: async (creds: AuthRequest): Promise<ApiResponse<AuthResponse>> => {
      const res = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(creds),
      });
      return handleResponse(res);
    },
    register: async (user: UsuarioDTO): Promise<ApiResponse<UsuarioDTO>> => {
      const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user),
      });
      return handleResponse(res);
    }
  },
  companies: {
    getAll: async (): Promise<ApiResponse<EmpresaDTO[]>> => {
      const res = await fetch(`${API_URL}/empresas`, { headers: getHeaders() });
      return handleResponse(res);
    },
    getOne: async (nit: string): Promise<ApiResponse<EmpresaDTO>> => {
      const res = await fetch(`${API_URL}/empresas/${nit}`, { headers: getHeaders() });
      return handleResponse(res);
    },
    create: async (company: EmpresaDTO): Promise<ApiResponse<EmpresaDTO>> => {
      const res = await fetch(`${API_URL}/empresas`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(company),
      });
      return handleResponse(res);
    },
    update: async (nit: string, company: EmpresaDTO): Promise<ApiResponse<EmpresaDTO>> => {
      const res = await fetch(`${API_URL}/empresas/${nit}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify(company),
      });
      return handleResponse(res);
    },
    delete: async (nit: string): Promise<ApiResponse<void>> => {
      const res = await fetch(`${API_URL}/empresas/${nit}`, {
        method: 'DELETE',
        headers: getHeaders(),
      });
      return handleResponse(res);
    }
  },
  products: {
    getAll: async (): Promise<ApiResponse<ProductoDTO[]>> => {
      const res = await fetch(`${API_URL}/productos`, { headers: getHeaders() });
      return handleResponse(res);
    },
    getByCompany: async (nit: string): Promise<ApiResponse<ProductoDTO[]>> => {
      const res = await fetch(`${API_URL}/productos/empresa/${nit}`, { headers: getHeaders() });
      return handleResponse(res);
    },
    create: async (product: ProductoDTO): Promise<ApiResponse<ProductoDTO>> => {
      const res = await fetch(`${API_URL}/productos`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(product),
      });
      return handleResponse(res);
    },
    update: async (id: number, product: ProductoDTO): Promise<ApiResponse<ProductoDTO>> => {
      const res = await fetch(`${API_URL}/productos/${id}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify(product),
      });
      return handleResponse(res);
    },
    delete: async (id: number): Promise<ApiResponse<void>> => {
      const res = await fetch(`${API_URL}/productos/${id}`, {
        method: 'DELETE',
        headers: getHeaders(),
      });
      return handleResponse(res);
    }
  },
  inventory: {
    sendReport: async (report: InventoryReportRequest): Promise<ApiResponse<string>> => {
      const res = await fetch(`${API_URL}/inventory/report/send`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(report),
      });
      return handleResponse(res);
    }
  }
};