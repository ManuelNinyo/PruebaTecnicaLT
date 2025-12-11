import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import { EmpresaDTO } from '../types';
import { Button, Input, Select } from '../components/UI';
import { Send, FileText, CheckCircle2 } from 'lucide-react';

const Inventory: React.FC = () => {
  const [companies, setCompanies] = useState<EmpresaDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    toEmail: '',
    subject: 'Inventory Report',
    body: 'Please find attached the latest inventory report.',
    empresaNit: ''
  });

  useEffect(() => {
    api.companies.getAll().then(res => {
        if(res.success) setCompanies(res.data);
    });
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const res = await api.inventory.sendReport(formData);
      if (res.success) {
        setSuccess('Report sent successfully!');
        setFormData(prev => ({ ...prev, toEmail: '' })); // clear email
      } else {
        setError(res.message || 'Failed to send report');
      }
    } catch (err) {
      setError('An error occurred while communicating with the server.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center gap-3">
        <div className="p-3 bg-blue-100 rounded-lg">
            <FileText className="text-blue-600 w-6 h-6" />
        </div>
        <h1 className="text-2xl font-bold text-gray-900">Inventory Reporting</h1>
      </div>
      
      <p className="text-gray-500">
        Generate and send a PDF inventory report to stakeholders via email. You can filter the report by company.
      </p>

      <div className="bg-white shadow rounded-xl p-6 border border-gray-100">
        <form onSubmit={handleSubmit} className="space-y-6">
          <Select
            label="Filter by Company (Optional)"
            value={formData.empresaNit}
            onChange={(e) => setFormData({ ...formData, empresaNit: e.target.value })}
          >
            <option value="">All Companies</option>
            {companies.map(c => (
                <option key={c.nit} value={c.nit}>{c.nombre} (NIT: {c.nit})</option>
            ))}
          </Select>

          <Input
            label="Recipient Email"
            type="email"
            required
            placeholder="manager@example.com"
            value={formData.toEmail}
            onChange={(e) => setFormData({ ...formData, toEmail: e.target.value })}
          />

          <Input
            label="Subject"
            required
            value={formData.subject}
            onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
          />

          <div className="space-y-1">
            <label className="block text-sm font-medium text-gray-700">Message Body</label>
            <textarea
              className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              rows={4}
              value={formData.body}
              onChange={(e) => setFormData({ ...formData, body: e.target.value })}
            />
          </div>

          {error && (
            <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm">
              {error}
            </div>
          )}

          {success && (
            <div className="bg-green-50 text-green-700 p-3 rounded-lg text-sm flex items-center gap-2">
              <CheckCircle2 size={16} />
              {success}
            </div>
          )}

          <div className="flex justify-end pt-4">
            <Button type="submit" isLoading={loading}>
              <Send size={16} className="mr-2" />
              Send Report
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Inventory;