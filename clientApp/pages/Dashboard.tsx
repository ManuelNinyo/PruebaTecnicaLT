import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import { Building2, Package, Users } from 'lucide-react';
import { Link } from 'react-router-dom';

const StatCard = ({ label, value, icon: Icon, to, color }: { label: string, value: number, icon: any, to: string, color: string }) => (
  <Link to={to} className="block transform transition-transform hover:-translate-y-1">
    <div className="bg-white overflow-hidden shadow rounded-lg">
      <div className="p-5">
        <div className="flex items-center">
          <div className="flex-shrink-0">
            <div className={`rounded-md p-3 ${color} bg-opacity-10`}>
              <Icon className={`h-6 w-6 ${color.replace('bg-', 'text-')}`} />
            </div>
          </div>
          <div className="ml-5 w-0 flex-1">
            <dl>
              <dt className="text-sm font-medium text-gray-500 truncate">{label}</dt>
              <dd>
                <div className="text-lg font-medium text-gray-900">{value}</div>
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  </Link>
);

const Dashboard: React.FC = () => {
  const [counts, setCounts] = useState({ companies: 0, products: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [companiesRes, productsRes] = await Promise.all([
          api.companies.getAll(),
          api.products.getAll()
        ]);
        setCounts({
          companies: companiesRes.data?.length || 0,
          products: productsRes.data?.length || 0
        });
      } catch (error) {
        console.error("Failed to load dashboard stats", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
      
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
        <StatCard 
          label="Total Companies" 
          value={loading ? 0 : counts.companies} 
          icon={Building2} 
          to="/companies"
          color="bg-indigo-600"
        />
        <StatCard 
          label="Total Products" 
          value={loading ? 0 : counts.products} 
          icon={Package} 
          to="/products"
          color="bg-green-600"
        />
        <div className="bg-white overflow-hidden shadow rounded-lg p-5">
           <div className="flex items-center">
             <div className="flex-shrink-0">
               <div className="rounded-md p-3 bg-blue-100">
                 <Users className="h-6 w-6 text-blue-600" />
               </div>
             </div>
             <div className="ml-5">
               <p className="text-sm font-medium text-gray-500">Welcome Back</p>
               <p className="text-sm text-gray-900 mt-1">Manage your inventory efficiently.</p>
             </div>
           </div>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg p-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900 mb-4">Quick Actions</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Link to="/inventory" className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
            <h4 className="font-semibold text-blue-600">Send Inventory Report</h4>
            <p className="text-sm text-gray-500 mt-1">Generate and email the latest inventory status.</p>
          </Link>
          <Link to="/companies" className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
            <h4 className="font-semibold text-blue-600">Add New Company</h4>
            <p className="text-sm text-gray-500 mt-1">Register a new business entity to the system.</p>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;