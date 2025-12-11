import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { ProductoDTO, EmpresaDTO } from '../types';
import { Button, Input, Modal, Table, Select } from '../components/UI';
import { Edit2, Trash2, Plus, Filter } from 'lucide-react';

const Products: React.FC = () => {
  const [products, setProducts] = useState<ProductoDTO[]>([]);
  const [companies, setCompanies] = useState<EmpresaDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [filterCompany, setFilterCompany] = useState('');

  const initialFormState: ProductoDTO = {
    codigo: '',
    nombre: '',
    precio: 0,
    moneda: 'USD',
    caracteristicas: '',
    empresaNit: '',
    categoriaId: 1 // Default for demo
  };

  const [formData, setFormData] = useState<ProductoDTO>(initialFormState);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [prodRes, compRes] = await Promise.all([
        filterCompany ? api.products.getByCompany(filterCompany) : api.products.getAll(),
        api.companies.getAll()
      ]);
      if (prodRes.success) setProducts(prodRes.data);
      if (compRes.success) setCompanies(compRes.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [filterCompany]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.products.update(editingId, formData);
      } else {
        await api.products.create(formData);
      }
      setIsModalOpen(false);
      fetchData(); // Refresh list
    } catch (error) {
      alert('Operation failed');
    }
  };

  const openEdit = (prod: ProductoDTO) => {
    setEditingId(prod.id!);
    setFormData(prod);
    setIsModalOpen(true);
  };

  const openCreate = () => {
    setEditingId(null);
    setFormData(initialFormState);
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Delete this product?')) return;
    try {
      await api.products.delete(id);
      fetchData();
    } catch (error) {
      alert('Delete failed');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <h1 className="text-2xl font-bold text-gray-900">Products</h1>
        <div className="flex gap-2 w-full sm:w-auto">
            <div className="relative flex-1 sm:w-64">
                <Filter className="absolute left-3 top-2.5 h-4 w-4 text-gray-400" />
                <select 
                    className="pl-9 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 border-gray-300 bg-white"
                    value={filterCompany}
                    onChange={(e) => setFilterCompany(e.target.value)}
                >
                    <option value="">All Companies</option>
                    {companies.map(c => <option key={c.nit} value={c.nit}>{c.nombre}</option>)}
                </select>
            </div>
            <Button onClick={openCreate}><Plus size={18} className="mr-2"/> New Product</Button>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-10">Loading...</div>
      ) : (
        <Table headers={['Code', 'Name', 'Price', 'Company', 'Characteristics', 'Actions']}>
          {products.map((p) => {
            const companyName = companies.find(c => c.nit === p.empresaNit)?.nombre || p.empresaNit;
            return (
                <tr key={p.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{p.codigo}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{p.nombre}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{p.precio} {p.moneda}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{companyName}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 truncate max-w-xs">{p.caracteristicas}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                    <button onClick={() => openEdit(p)} className="text-indigo-600 hover:text-indigo-900"><Edit2 size={18} /></button>
                    <button onClick={() => handleDelete(p.id!)} className="text-red-600 hover:text-red-900"><Trash2 size={18} /></button>
                </td>
                </tr>
            )
          })}
          {products.length === 0 && (
            <tr>
              <td colSpan={6} className="px-6 py-8 text-center text-sm text-gray-500">No products found.</td>
            </tr>
          )}
        </Table>
      )}

      <Modal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        title={editingId ? 'Edit Product' : 'New Product'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
            <Select 
                label="Company"
                value={formData.empresaNit}
                onChange={e => setFormData({...formData, empresaNit: e.target.value})}
                required
            >
                <option value="">Select Company</option>
                {companies.map(c => <option key={c.nit} value={c.nit}>{c.nombre}</option>)}
            </Select>
            <div className="grid grid-cols-2 gap-4">
                <Input 
                    label="Code" 
                    value={formData.codigo} 
                    onChange={e => setFormData({...formData, codigo: e.target.value})} 
                    required
                />
                <Input 
                    label="Name" 
                    value={formData.nombre} 
                    onChange={e => setFormData({...formData, nombre: e.target.value})} 
                    required
                />
            </div>
            <div className="grid grid-cols-2 gap-4">
                <Input 
                    label="Price" 
                    type="number"
                    value={formData.precio} 
                    onChange={e => setFormData({...formData, precio: parseFloat(e.target.value)})} 
                    required
                />
                <Input 
                    label="Currency" 
                    value={formData.moneda} 
                    onChange={e => setFormData({...formData, moneda: e.target.value})} 
                />
            </div>
            <Input 
                label="Characteristics" 
                value={formData.caracteristicas || ''} 
                onChange={e => setFormData({...formData, caracteristicas: e.target.value})} 
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

export default Products;