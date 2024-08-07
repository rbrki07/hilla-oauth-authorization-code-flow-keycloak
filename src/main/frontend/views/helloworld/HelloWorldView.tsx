import { Button } from '@vaadin/react-components/Button.js';
import { Notification } from '@vaadin/react-components/Notification.js';
import { TextField } from '@vaadin/react-components/TextField.js';
import { HelloWorldService } from 'Frontend/generated/endpoints.js';
import { useAuth } from 'Frontend/util/auth';
import { useState } from 'react';

export default function HelloWorldView() {
  const { state } = useAuth();
  const [name, setName] = useState(state.user?.firstname ?? '');

  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          value={name}
          label="Your name"
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
        <Button
          onClick={async () => {
            const serverResponse = await HelloWorldService.sayHello(name);
            Notification.show(serverResponse, { position: 'middle' });
          }}
        >
          Say hello
        </Button>
      </section>
    </>
  );
}
