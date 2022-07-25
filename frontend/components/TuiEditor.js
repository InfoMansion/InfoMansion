import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/react-editor';

export default function setTuiEditor({}) {
  return (
    <>
      <Editor
        initialValue="write post"
        initialEditType="wysiwyg"
        height="70%"
      />
    </>
  );
}
