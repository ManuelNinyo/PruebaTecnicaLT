import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { EmpresaDTO } from '../types';
import { Button, Input, Modal, Table } from '../components/UI';
import { Edit2, Trash2, Plus, RefreshCw } from 'lucide-react';

const Companies: React.FC = () => {
  const [companies, setCompanies] = useState<EmpresaDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCompany, setEditingCompany] = useState<EmpresaDTO | null>(null);
  
  // Form State
  const [formData, setFormData] = useState<EmpresaDTO>({
    nit: '',
    nombre: '',
    direccion: '',
    telefono: ''
  });

  const fetchCompanies = async () => {
    setLoading(true);
    try {
      const res = await api.companies.getAll();
      if (res.success) setCompanies(res.data);
    } catch (error) {
      console.error(error);
      alert('Failed to load companies');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCompanies();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCompany) {
        await api.companies.update(editingCompany.nit, formData);
      } else {
        await api.companies.create(formData);
      }
      setIsModalOpen(false);
      fetchCompanies();
    } catch (error) {
      alert('Operation failed');
    }
  };

  const openEdit = (company: EmpresaDTO) => {
    setEditingCompany(company);
    setFormData(company);
    setIsModalOpen(true);
  };

  const openCreate = () => {
    setEditingCompany(null);
    setFormData({ nit: '', nombre: '', direccion: '', telefono: '' });
    setIsModalOpen(true);
  };

  const handleDelete = async (nit: string) => {
    if (!window.confirm('Are you sure you want to delete this company?')) return;
    try {
      await api.companies.delete(nit);
      fetchCompanies();
    } catch (error) {
      alert('Failed to delete');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Companies</h1>
        <div className="flex gap-2">
            <Button variant="secondary" onClick={fetchCompanies}><RefreshCw size={18}/></Button>
            <Button onClick={openCreate}><Plus size={18} className="mr-2"/> New Company</Button>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-10">Loading...</div>
      ) : (
        <Table headers={['NIT', 'Name', 'Address', 'Phone', 'Actions']}>
          {companies.map((company) => (
            <tr key={company.nit} className="hover:bg-gray-50">
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{company.nit}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{company.nombre}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{company.direccion || '-'}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{company.telefono || '-'}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                <button onClick={() => openEdit(company)} className="text-indigo-600 hover:text-indigo-900"><Edit2 size={18} /></button>
                <button onClick={() => handleDelete(company.nit)} className="text-red-600 hover:text-red-900"><Trash2 size={18} /></button>
              </td>
            </tr>
          ))}
          {companies.length === 0 && (
            <tr>
              <td colSpan={5} className="px-6 py-8 text-center text-sm text-gray-500">No companies found.</td>
            </tr>
          )}
        </Table>
      )}

      <Modal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        title={editingCompany ? 'Edit Company' : 'New Company'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input 
            label="NIT" 
            value={formData.nit} 
            onChange={e => setFormData({...formData, nit: e.target.value})} 
            disabled={!!editingCompany}
            required
            placeholder="e.g., 900123456"
          />
          <Input 
            label="Name" 
            value={formData.nombre} 
            onChange={e => setFormData({...formData, nombre: e.target.value})} 
            required
            placeholder="Company Legal Name"
          />
          <Input 
            label="Address" 
            value={formData.direccion || ''} 
            onChange={e => setFormData({...formData, direccion: e.target.value})} 
            placeholder="123 Business Rd"
          />
          <Input 
            label="Phone" 
            value={formData.telefono || ''} 
            onChange={e => setFormData({...formData, telefono: e.target.value})} 
            placeholder="+1 555 123 4567"
          />
          <div className="pt-2 flex justify-end gap-2">
            <Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>Cancel</Button>
            <Button type="submit">Save</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Companies;