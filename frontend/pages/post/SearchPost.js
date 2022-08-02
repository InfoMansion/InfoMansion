import * as React from 'react';
import { useRouter } from 'next/router';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import { MAIN_COLOR } from '../../constants';
import TabPanel from '../../components/PostPage/TabPanel';

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function SearchPost() {
  //검색 결과가 담기는 query입니다.
  const { query } = useRouter();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  const styles = theme => ({
    indicator: {
      backgroundColor: 'white',
    },
  });
  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs
          value={value}
          onChange={handleChange}
          textColorPrimary="black"
          sx={{
            '.MuiButtonBase-root': {
              '&.Mui-selected': {
                color: MAIN_COLOR,
              },
            },
          }}
          TabIndicatorProps={{
            style: {
              backgroundColor: MAIN_COLOR,
            },
          }}
        >
          <Tab label="제목" {...a11yProps(0)} />
          <Tab label="내용" {...a11yProps(1)} />
          <Tab label="닉네임" {...a11yProps(2)} />
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        제목 관련 검색 결과의 포스트들이 오겠죠?
      </TabPanel>
      <TabPanel value={value} index={1}>
        내용 관련 검색 결과의 포스트들이 오겠죠?
      </TabPanel>
      <TabPanel value={value} index={2}>
        닉네임 관련 검색 결과의 포스트들이 오겠죠?
      </TabPanel>
    </Box>
  );
}
