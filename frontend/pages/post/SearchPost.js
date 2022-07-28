import { useRouter } from 'next/router';

export default function searchPost() {
  const { query } = useRouter();
  console.log(query);
  return (
    <>
      <h1>searchPost</h1>
      <div>{query}</div>
    </>
  );
}
