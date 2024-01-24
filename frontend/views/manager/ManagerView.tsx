export default function ManagerView() {
    return (
      <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
        <h2>Manager view</h2>
        <p>This view is only visible for users with the role 'Manager'.</p>
      </div>
    );
  }
  