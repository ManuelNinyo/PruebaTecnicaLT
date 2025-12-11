import React, { ReactNode } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link, useLocation } from 'react-router-dom';
import { 
  Building2, 
  Package, 
  FileText, 
  LogOut, 
  LayoutDashboard, 
  Menu,
  X
} from 'lucide-react';

interface LayoutProps {
  children: ReactNode;
}

const NavItem = ({ to, icon: Icon, label, active }: { to: string, icon: any, label: string, active: boolean }) => (
  <Link
    to={to}
    className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
      active 
        ? 'bg-blue-600 text-white' 
        : 'text-gray-600 hover:bg-gray-100'
    }`}
  >
    <Icon size={20} />
    <span className="font-medium">{label}</span>
  </Link>
);

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { logout, user } = useAuth();
  const location = useLocation();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = React.useState(false);

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <aside className={`
        fixed inset-y-0 left-0 z-50 w-64 bg-white border-r border-gray-200 transform transition-transform duration-200 ease-in-out
        ${isMobileMenuOpen ? 'translate-x-0' : '-translate-x-full'}
        md:translate-x-0 md:relative
      `}>
        <div className="h-full flex flex-col">
          <div className="p-6 border-b border-gray-100 flex items-center justify-between">
            <h1 className="text-xl font-bold text-blue-600 flex items-center gap-2">
              <Building2 className="text-blue-600" />
              EmpresaMgr
            </h1>
            <button className="md:hidden" onClick={() => setIsMobileMenuOpen(false)}>
              <X size={20} />
            </button>
          </div>

          <nav className="flex-1 p-4 space-y-2">
            <NavItem 
              to="/" 
              icon={LayoutDashboard} 
              label="Dashboard" 
              active={location.pathname === '/'} 
            />
            <NavItem 
              to="/companies" 
              icon={Building2} 
              label="Companies" 
              active={location.pathname === '/companies'} 
            />
            <NavItem 
              to="/products" 
              icon={Package} 
              label="Products" 
              active={location.pathname === '/products'} 
            />
            <NavItem 
              to="/inventory" 
              icon={FileText} 
              label="Inventory Reports" 
              active={location.pathname === '/inventory'} 
            />
          </nav>

          <div className="p-4 border-t border-gray-100">
            <div className="mb-4 px-4">
              <p className="text-sm font-medium text-gray-900">{user?.email}</p>
              <p className="text-xs text-gray-500 capitalize">{user?.role?.toLowerCase()}</p>
            </div>
            <button
              onClick={logout}
              className="w-full flex items-center gap-3 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
            >
              <LogOut size={20} />
              <span className="font-medium">Sign Out</span>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col min-w-0 overflow-hidden">
        {/* Mobile Header */}
        <header className="md:hidden bg-white border-b border-gray-200 p-4 flex items-center justify-between">
          <span className="font-bold text-blue-600">EmpresaMgr</span>
          <button onClick={() => setIsMobileMenuOpen(true)}>
            <Menu size={24} className="text-gray-600" />
          </button>
        </header>

        <div className="flex-1 overflow-auto p-4 md:p-8">
          {children}
        </div>
      </main>
    </div>
  );
};

export default Layout;