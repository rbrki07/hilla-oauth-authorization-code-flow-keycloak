import { configureAuth } from '@hilla/react-auth';
import { UserAuthenticationService } from 'Frontend/generated/endpoints';

const auth = configureAuth(UserAuthenticationService.getAuthenticatedUser);

export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;