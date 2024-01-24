import { AppLayout } from '@hilla/react-components/AppLayout.js';
import { Avatar } from '@hilla/react-components/Avatar.js';
import { Button } from '@hilla/react-components/Button.js';
import { DrawerToggle } from '@hilla/react-components/DrawerToggle.js';
import Placeholder from 'Frontend/components/placeholder/Placeholder.js';
import { useRouteMetadata } from 'Frontend/util/routing.js';
import { Suspense, useEffect } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from 'Frontend/util/auth.js';
import { UserAuthenticationService } from 'Frontend/generated/endpoints';

const navLinkClasses = ({ isActive }: any) => {
  return `block rounded-m p-s ${isActive ? 'bg-primary-10 text-primary' : 'text-body'}`;
};

export default function MainLayout() {
  const currentTitle = useRouteMetadata()?.title ?? 'My App';
  const { state, logout } = useAuth();
  useEffect(() => {
    document.title = currentTitle;
  }, [currentTitle]);

  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
          <h1 className="text-l m-0">My App</h1>
          <nav>
            <NavLink className={navLinkClasses} to="/">
              Hello World
            </NavLink>
            <NavLink className={navLinkClasses} to="/about">
              About
            </NavLink>
            {state.user?.roles.indexOf('IC') !== -1 && (
              <NavLink className={navLinkClasses} to="/ic">
                IC
              </NavLink>
            )}
            {state.user?.roles.indexOf('Manager') !== -1 && (
              <NavLink className={navLinkClasses} to="/manager">
                Manager
              </NavLink>
            )}
          </nav>
        </header>
        <footer className="flex flex-col gap-s">
          {state.user ? (
            <>
              <div className="flex items-center gap-s">
              <Avatar theme="xsmall" name={state.user.username} />
                {state.user.firstname} {state.user.lastname}
              </div>
              <Button onClick={async () => {
                const logoutUrl = await UserAuthenticationService.getLogoutUrl()
                await logout()
                window.location.href = logoutUrl
              }}>Sign out</Button>
            </>
          ) : (
            <a href="/oauth2/authorization/keycloak">Sign in</a>
          )}
        </footer>
      </div>

      <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h2 slot="navbar" className="text-l m-0">
        {currentTitle}
      </h2>

      <Suspense fallback={<Placeholder />}>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
