INSERT INTO public.role(name) VALUES('ROLE_USER') ON CONFLICT (name) do NOTHING;
INSERT INTO public.role(name) VALUES('ROLE_MODERATOR') ON CONFLICT (name) do NOTHING;
INSERT INTO public.role(name) VALUES('ROLE_ADMIN') ON CONFLICT (name) do NOTHING;